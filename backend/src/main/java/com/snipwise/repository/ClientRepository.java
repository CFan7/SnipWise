package com.snipwise.repository;

import com.snipwise.pojo.Client;

import java.util.List;

public interface ClientRepository
{
    void createClient(Client client);

    void updateClient(Client client);

    Client getClient(String email);
    Boolean hasClientExists(String email);

    void deleteClient(String email);

}
