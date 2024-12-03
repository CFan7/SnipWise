package com.snipwise.repository;

import com.snipwise.pojo.Client;

import java.util.List;

public interface ClientRepository
{
    void createClient(Client client);

    void updateClient(Client client);
    void initClientForCompany(String email, String companyName);
    void initClientForGroup(String email, String groupName);

    Client getClientByEmail(String email);
    Boolean hasClientExists(String email);

    Boolean hasClientOwnerOfCompany(String email, String companyName);
    Boolean hasClientAdminOfCompany(String email, String companyName);
    Boolean hasClientMemberOfCompany(String email, String companyName);

    Boolean hasClientOwnerOfGroup(String email, String groupId);
    Boolean hasClientAdminOfGroup(String email, String groupId);
    Boolean hasClientWriteMemberOfGroup(String email, String groupId);
    Boolean hasClientMemberOfGroup(String email, String groupId);

    List<String> getCompanyOwners(String email);
    List<String> getCompanyAdmins(String email);
    List<String> getCompanyMembers(String email);

    List<String> getGroupOwners(String clientEmail);
    List<String> getGroupAdmins(String clientEmail);
    List<String> getGroupWriteMembers(String clientEmail);
    List<String> getGroupMembers(String clientEmail);

    void deleteClient(String email);







}
