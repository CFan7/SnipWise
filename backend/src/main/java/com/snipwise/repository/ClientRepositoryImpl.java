package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;
import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.OptimisticLockException;
import com.snipwise.pojo.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ClientRepositoryImpl implements ClientRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    TableId usersTableID = TableId.of("snip-wise_clients");

    @Override
    public Boolean hasClientExists(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        return row != null;
    }

    @Override
    public Client getClient(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        return new Client(email,row);
    }

    @Override
    public void createClient(Client client) {

        Row row = bigtableDataClient.readRow(usersTableID, client.client_email());
        if(row != null)
        {
            throw new ClientAlreadyExistException();
        }
        RowMutation mutation = RowMutation.create(
                usersTableID,
                client.client_email())
                .setCell("default","version",client.version())
                .setCell("default","clientName",client.client_name())
                .setCell("default","passwd_encrypted",client.passwd_encrypted())
                .setCell("default", "company_owners",String.join(";;",client.company_owners()))
                .setCell("default","company_admins",String.join(";;",client.company_admins()))
                .setCell("default","company_members",String.join(";;",client.company_members()))
                .setCell("default","group_owners",String.join(";;",client.group_owners()))
                .setCell("default","group_admins",String.join(";;",client.group_admins()))
                .setCell("default","group_write_members",String.join(";;",client.group_write_members()))
                .setCell("default","group_members",String.join(";;",client.group_members()));

        bigtableDataClient.mutateRow(mutation);
    }

    @Override
    public void updateClient(Client client)
    {
        Long currentVersion = Long.parseLong(client.version());
        Filters.Filter filter = Filters.FILTERS.chain()
                .filter(Filters.FILTERS.qualifier().exactMatch("version"))
                .filter(Filters.FILTERS.value().range().startClosed(String.valueOf(currentVersion)).endOpen(String.valueOf(currentVersion + 1)));
        // Optimistic Locking
        ConditionalRowMutation conditionalRowMutation =
                ConditionalRowMutation.create(usersTableID, client.client_email())
                        .condition(filter)
                        .then(
                                Mutation.create()
                                        .setCell("default","version",String.valueOf(currentVersion + 1))
                                        .setCell("default","clientName",client.client_name())
                                        .setCell("default","passwd_encrypted",client.passwd_encrypted())
                                        .setCell("default", "company_owners",String.join(";;",client.company_owners()))
                                        .setCell("default","company_admins",String.join(";;",client.company_admins()))
                                        .setCell("default","company_members",String.join(";;",client.company_members()))
                                        .setCell("default","group_owners",String.join(";;",client.group_owners()))
                                        .setCell("default","group_admins",String.join(";;",client.group_admins()))
                                        .setCell("default","group_write_members",String.join(";;",client.group_write_members()))
                                        .setCell("default","group_members",String.join(";;",client.group_members()))

                        );

        boolean mutationApplied = bigtableDataClient.checkAndMutateRow(conditionalRowMutation);
        if (!mutationApplied) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void deleteClient(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        RowMutation mutation = RowMutation.create(usersTableID,email).deleteRow();

        bigtableDataClient.mutateRow(mutation);
    }
}
