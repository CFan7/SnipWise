package com.snipwise.repository;

import com.google.bigtable.v2.ReadRowsRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;
import com.snipwise.exception.URLRecordNotExistException;
import com.snipwise.pojo.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class URLRepositoryImpl implements URLRepository
{


    @Autowired
    BigtableDataClient bigtableDataClient;
    TableId usersTableID = TableId.of("snip-wise");
    @Override
    public URL getRecordByShortURL(String shortUrl)
    {
        Row row = bigtableDataClient.readRow(usersTableID, shortUrl);
        if (row == null)
        {
            throw new URLRecordNotExistException();
        }
        return new URL(shortUrl,row);

    }

    @Override
    public void createURLRecord(URL entity) {
        String short_url = entity.shortUrl();
        if (bigtableDataClient.readRow(usersTableID, short_url) != null) {
            System.out.println(short_url + "already exists in table");
            return;
        }
        RowMutation mutation = RowMutation.create(
                        usersTableID,
                        short_url)
                .setCell("default", "originalUrl", entity.originalUrl())
                .setCell("default", "expirationTime", entity.expirationTime().toEpochSecond())
                .setCell("default","isActivated",entity.isActivated()?1L:0L)
                .setCell("default","groupId",entity.groupId());
        bigtableDataClient.mutateRow(mutation);
    }

    @Override
    public void deleteURLRecord(String shortUrl) {
        Row row = bigtableDataClient.readRow(usersTableID, shortUrl);
        if (row == null)
        {
            throw new URLRecordNotExistException();
        }
        RowMutation mutation = RowMutation.create(usersTableID,shortUrl).deleteRow();

        bigtableDataClient.mutateRow(mutation);
    }

    @Override
    public List<URL> getGroupURLs(String groupId)
    {
        Filters.Filter filter = Filters.FILTERS.chain()
                .filter(Filters.FILTERS.family().exactMatch("default"))
                .filter(Filters.FILTERS.qualifier().exactMatch("groupId"))
                .filter(Filters.FILTERS.value().exactMatch(groupId));

        Query query = Query.create(usersTableID).filter(filter);
        List<URL> urls = new ArrayList<>();
        for (Row row : bigtableDataClient.readRows(query))
        {
            String key = row.getKey().toStringUtf8();
            urls.add(getRecordByShortURL(key));
        }



        return urls;
    }

    /*
    void close()
    {
        this.bigtableDataClient.close();
    }
     */
}

/*
public void createClient(DemoUser user) {
        String username = user.username();
        if (client.readRow(usersTableID, username) != null) {
            System.out.println("User \"" + username + "\" already exists in table");
            return;
        }

        RowMutation mutation = RowMutation.create(
                        usersTableID,
                        username)
                .setCell("User", "username", username)
                .setCell("User", "color", user.color())
                .setCell("User", "timestamp", user.timestamp());
        client.mutateRow(mutation);
        System.out.println("Successfully wrote user \"" + username + "\" to DB.");
    }
    String getUserColor(String username) {
        Row row = client.readRow(usersTableID, username);
        if (row == null) return "";
        return row.getCells("User", "color").get(0).getValue().toStringUtf8();
    }
* */