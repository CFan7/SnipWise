package com.snipwise.repository;
/*
import com.google.cloud.bigtable.data.v2.BigtableDataClient;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BigTable {
}

import com.comp413.bigtabledemo.obj.DemoUser;
import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BigTableManager {

    private String usersTableID;
    private final BigtableDataClient client;
    private final Random RANDOM = new Random(0xC413 + Instant.now().getEpochSecond());

    public BigTableManager(String projectId, String instanceId, String tableID) throws IOException {

//        BigtableTableAdminClient admin = BigtableTableAdminClient.create(projectId, instanceId);
//        admin.dropAllRows(usersTableID);
//        admin.close();
        usersTableID = tableID;
        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder()
                        .setProjectId(projectId)
                        .setInstanceId(instanceId)
                        .build();

        this.client = BigtableDataClient.create(settings);
    }

    void close() {
        this.client.close();
    }

    List<DemoUser> getUsers() {
        List<DemoUser> users = new ArrayList<>();
//        Stream<Row> rows = client.readRows(Query.create(usersTableID)).stream();
        Iterator<Row> rows = client.readRows(Query.create(usersTableID)).stream().iterator();
        Row row;
        while (rows.hasNext()) {
            row = rows.next();
            String username =  row.getCells("User", "username").get(0).getValue().toStringUtf8();
            String color = row.getCells("User", "color").get(0).getValue().toStringUtf8();
            String registrationTimestamp =  row.getCells("User", "timestamp").get(0).getValue().toStringUtf8();
            users.add(new DemoUser(username, color, registrationTimestamp));
        }
        return users;
    }

    String getUserColor(String username) {
        Row row = client.readRow(usersTableID, username);
        if (row == null) return "";
        return row.getCells("User", "color").get(0).getValue().toStringUtf8();
    }

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

    public void updateColor(String username, String value) {
        Row row = client.readRow(usersTableID, username);
        if (row == null) return;
        String oldColor = row.getCells("User", "color").get(0).getValue().toStringUtf8();
        if (!value.equals(oldColor)) {
            client.mutateRow(
                    RowMutation.create(usersTableID, username)
                            .setCell("User", "color", String.valueOf(value))
            );
        }
    }

    public void deleteUser(String username) {

        Row row = client.readRow(usersTableID, username);
        if (row == null) return;
        client.mutateRow(
                RowMutation.create(usersTableID, username)
                        .deleteRow());
    }

}
*/
