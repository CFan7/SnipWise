package com.snipwise.service;

import com.snipwise.pojo.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface ClientService
{
    ClientCreateResponseDTO createClient(ClientCreateDTO client_create_dto);

    List<String> getGroupMembers(String jwtString, String clientEmail);

    void initClientForGroup(String email, String groupName);
    void initClientForCompany(String email, String companyName);


    Boolean isClientExist(String email);
    Boolean hasClientOwnerOfCompany(String email, String companyName);
    Boolean hasClientAdminOfCompany(String email, String companyName);
    Boolean hasClientOwnerOfGroup(String email, String groupId);
    Boolean hasClientAdminOfGroup(String email, String groupId);
    List<String> getCompanyOwners(String jwtString, String email);
    List<String> getCompanyAdmins(String jwtString, String email);
    List<String> getCompanyMembers(String jwtString, String email);
    List<String> getGroupOwners(String jwtString, String clientEmail);
    List<String> getGroupAdmins(String jwtString, String clientEmail);
    List<String> getGroupWriteMembers(String jwtString, String clientEmail);



    ClientLoginResponseDTO login(@RequestBody ClientLoginDTO client_login_dto);




    //ClientGetResponseDTO getClient(String jwtString, String clientId);
}
