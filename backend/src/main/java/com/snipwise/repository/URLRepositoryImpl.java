package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.snipwise.exception.URLRecordNotExistException;
import com.snipwise.pojo.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class URLRepositoryImpl implements URLRepository
{

    @Autowired
    BigtableDataClient bigtableDataClient;
    String usersTableID = "snip-wise";

    @Override
    public URL getRecordByShortURL(String short_url)
    {
        Row row = bigtableDataClient.readRow(usersTableID, short_url);
        if (row == null)
        {
            throw new URLRecordNotExistException();
        }
        return new URL(short_url,row);

    }

    @Override
    public void createURLRecord(URL entity) {
        String short_url = entity.short_url();
        if (bigtableDataClient.readRow(usersTableID, short_url) != null) {
            System.out.println(short_url + "already exists in table");
            return;
        }
        RowMutation mutation = RowMutation.create(
                        usersTableID,
                        short_url)
                .setCell("default", "original_url", entity.original_url())
                .setCell("default", "expiration_time", Long.toString(entity.expiration_time_unix()))
                .setCell("default","isActivated",entity.isActivated()?"true":"false")
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