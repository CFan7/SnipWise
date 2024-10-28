package com.snipwise.repository;

import java.math.BigInteger;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.snipwise.pojo.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Component
public class BigTableRepositoryImpl implements BigtableRepository {

    @Autowired
    BigtableDataClient bigtableDataClient;
    String usersTableID = "snip-wise";

    @Override
    public URL getRecordByShortURL(String short_url)
    {
        Row row = bigtableDataClient.readRow(usersTableID, short_url);
        if (row == null)
        {
            return null;
        };

        String original_url=row.getCells("default","original_url").get(0).getValue().toStringUtf8();
        String expiration_time_str= row.getCells("default","expiration_time").get(0).getValue().toStringUtf8();
        Long expiration_time = Long.parseLong(expiration_time_str);

        String isDeleted_str = row.getCells("default","isDeleted").get(0).getValue().toStringUtf8();
        String isActivated_str = row.getCells("default","isActivated").get(0).getValue().toStringUtf8();
        Boolean isActivated = Boolean.parseBoolean(isActivated_str);
        Boolean isDeleted = Boolean.parseBoolean(isDeleted_str);

        String group_id = row.getCells("default","group_id").get(0).getValue().toStringUtf8();
        String creator_api_key = row.getCells("default","creator_api_key").get(0).getValue().toStringUtf8();

        return new URL(short_url,original_url,expiration_time,isDeleted,isActivated,group_id,creator_api_key);

    }

    @Override
    public void save(URL entity) {
        String short_url = entity.short_url();
        if (bigtableDataClient.readRow(usersTableID, short_url) != null) {
            System.out.println(short_url + "already exists in table");
            return;
        }
        RowMutation mutation = RowMutation.create(
                        usersTableID,
                        short_url)
                .setCell("default", "original_url", entity.original_url())
                .setCell("default", "expiration_time", Long.toString(entity.expiration_time()))
                .setCell("default", "isDeleted",entity.isDeleted()?"true":"false")
                .setCell("default","isActivated",entity.isActivated()?"true":"false")
                .setCell("default","group_id",entity.group_id())
                .setCell("default","creator_api_key",entity.creator_api_key());

        bigtableDataClient.mutateRow(mutation);
    }
    void close()
    {
        this.bigtableDataClient.close();
    }
}

/*
public void createUser(DemoUser user) {
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