package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
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
    String usersTableID = "snip-wise_clients";

    private ArrayList<String> toStringArrayList(String[] arr)
    {
        if (arr.length == 1 && arr[0].isEmpty())
        {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(arr));
    }

    @Override
    public Boolean hasClientExists(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        return row != null;
    }
    @Override
    public void initClientForCompany(String email, String companyName)
    {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        Client client = new Client(email,row);
        client.company_owners().add(companyName);
        client.company_admins().add(companyName);
        client.company_members().add(companyName);

        updateClient(client);
    }

    @Override
    public void initClientForGroup(String email, String groupName) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        Client client = new Client(email,row);
        client.group_owners().add(groupName);
        client.group_admins().add(groupName);
        client.group_write_members().add(groupName);
        client.group_members().add(groupName);

        updateClient(client);
    }

    @Override
    public Boolean hasClientOwnerOfCompany(String email, String companyName)
    {
        return getCompanyOwners(email).contains(companyName);
    }

    @Override
    public Boolean hasClientAdminOfCompany(String email, String companyName)
    {
        return getCompanyAdmins(email).contains(companyName);
    }

    @Override
    public Boolean hasClientMemberOfCompany(String email, String companyName) {
        return getCompanyMembers(email).contains(companyName);
    }

    @Override
    public Boolean hasClientOwnerOfGroup(String email, String groupId) {
        return getGroupOwners(email).contains(groupId);
    }


    @Override
    public Boolean hasClientAdminOfGroup(String email, String groupId) {
        return getGroupAdmins(email).contains(groupId);
    }

    @Override
    public Boolean hasClientWriteMemberOfGroup(String email, String groupId) {
        return getGroupWriteMembers(email).contains(groupId);
    }

    @Override
    public Boolean hasClientMemberOfGroup(String email, String groupId) {
        return getGroupMembers(email).contains(groupId);
    }

    @Override
    public List<String> getCompanyOwners(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] companyOwners = row.getCells("default","company_owners").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(companyOwners);
    }

    @Override
    public List<String> getCompanyAdmins(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] companyAdmins = row.getCells("default","company_admins").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(companyAdmins);
    }

    @Override
    public List<String> getCompanyMembers(String email) {
        Row row = bigtableDataClient.readRow(usersTableID, email);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] companyMembers = row.getCells("default","company_members").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(companyMembers);
    }
    @Override
    public List<String> getGroupOwners(String clientEmail) {
        Row row = bigtableDataClient.readRow(usersTableID, clientEmail);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] groupOwners = row.getCells("default","group_owners").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(groupOwners);
    }
    @Override
    public List<String> getGroupAdmins(String clientEmail) {
        Row row = bigtableDataClient.readRow(usersTableID, clientEmail);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] groupAdmins = row.getCells("default","group_admins").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(groupAdmins);
    }

    @Override
    public List<String> getGroupWriteMembers(String clientEmail) {
        Row row = bigtableDataClient.readRow(usersTableID, clientEmail);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] groupWriteMembers = row.getCells("default","group_write_members").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(groupWriteMembers);
    }

    @Override
    public List<String> getGroupMembers(String clientEmail) {
        Row row = bigtableDataClient.readRow(usersTableID, clientEmail);
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        String[] groupMembers = row.getCells("default","group_members").get(0).getValue().toStringUtf8().split(";;");
        return toStringArrayList(groupMembers);
    }


    @Override
    public Client getClientByEmail(String email) {
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
                .setCell("default","client_name",client.client_name())
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
    public void updateClient(Client client) {
        Row row = bigtableDataClient.readRow(usersTableID, client.client_email());
        if (row == null)
        {
            throw new ClientNotExistException();
        }
        RowMutation mutation = RowMutation.create(
                usersTableID,
                client.client_email()
        )
            .setCell("default","client_name",client.client_name())
            .setCell("default","passwd_encrypted",client.passwd_encrypted())
            .setCell("default", "company_owners",String.join(";;",client.company_owners()))
            .setCell("default","company_admins",String.join(";;",client.company_admins()))
            .setCell("default","company_members",String.join(";;",client.company_members()))
            .setCell("default","company_clients",String.join(";;",client.group_owners()))
            .setCell("default","group_owners",String.join(";;",client.group_owners()))
            .setCell("default","group_admins",String.join(";;",client.group_admins()))
            .setCell("default","group_write_members",String.join(";;",client.group_write_members()))
            .setCell("default","group_members",String.join(";;",client.group_members()));

        bigtableDataClient.mutateRow(mutation);
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
