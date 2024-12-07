package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;
import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.OptimisticLockException;
import com.snipwise.pojo.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        Row row = bigtableDataClient.readRow(usersTableID, client.clientEmail());
        if(row != null)
        {
            throw new ClientAlreadyExistException();
        }
        RowMutation mutation = RowMutation.create(
                usersTableID,
                client.clientEmail())
                .setCell("default","version",client.version())
                .setCell("default","clientName",client.clientName())
                .setCell("default","passwdEncrypted",client.passwdEncrypted())
                .setCell("default", "companyOwners",String.join(";;",client.companyOwners()))
                .setCell("default","companyAdmins",String.join(";;",client.companyAdmins()))
                .setCell("default","companyMembers",String.join(";;",client.companyMembers()))
                .setCell("default","groupOwners",String.join(";;",client.groupOwners()))
                .setCell("default","groupAdmins",String.join(";;",client.groupAdmins()))
                .setCell("default","groupWriteMembers",String.join(";;",client.groupWriteMembers()))
                .setCell("default","groupMembers",String.join(";;",client.groupMembers()))
                .setCell("default","dateOfBirth",client.dateOfBirth().toString())
                .setCell("default","phoneNumber",client.phoneNumber());;

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
                ConditionalRowMutation.create(usersTableID, client.clientEmail())
                        .condition(filter)
                        .then(
                                Mutation.create()
                                        .setCell("default","version",String.valueOf(currentVersion + 1))
                                        .setCell("default","clientName",client.clientName())
                                        .setCell("default","passwdEncrypted",client.passwdEncrypted())
                                        .setCell("default", "companyOwners",String.join(";;",client.companyOwners()))
                                        .setCell("default","companyAdmins",String.join(";;",client.companyAdmins()))
                                        .setCell("default","companyMembers",String.join(";;",client.companyMembers()))
                                        .setCell("default","groupOwners",String.join(";;",client.groupOwners()))
                                        .setCell("default","groupAdmins",String.join(";;",client.groupAdmins()))
                                        .setCell("default","groupWriteMembers",String.join(";;",client.groupWriteMembers()))
                                        .setCell("default","groupMembers",String.join(";;",client.groupMembers()))
                                        .setCell("default","dateOfBirth",client.dateOfBirth().toString())
                                        .setCell("default","phoneNumber",client.phoneNumber())
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
