package com.snipwise.repository;

import com.snipwise.pojo.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT CASE WHEN count(c) > 0 THEN true ELSE false END FROM Company c WHERE c.company_name = :company_name")
    Boolean isCompanyExists(UUID company_name);

    //@Query("SELECT c from Client c WHERE c.client_email = :email")
    //Client getClientByEmail(String email);

    //@Query("SELECT c from Client c WHERE c.client_id = :clientId")
    //Client getClientByClientId(String clientId);
}
