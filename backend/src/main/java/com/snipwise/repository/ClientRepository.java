package com.snipwise.repository;

import com.snipwise.pojo.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("SELECT COUNT(c) FROM Client c WHERE c.client_email = :email")
    long countByEmail(String email);


    @Query("SELECT c from Client c WHERE c.client_email = :email")
    Client getClientByEmail(String email);

    @Query("SELECT c from Client c WHERE c.client_id = :clientId")
    Client getClientByClientId(String clientId);
}
