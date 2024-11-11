package com.snipwise.repository;

import com.snipwise.pojo.CompanyPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyPermissionRepository extends JpaRepository<CompanyPermission, UUID> {
    @Query("SELECT cp.permission_type FROM CompanyPermission cp where cp.company_id=:company_id AND cp.client_id =:client_id")
    String getPermissionTypeByCompanyId(String company_id, String client_id);
    //@Query("SELECT exists(c) FROM Company c WHERE c.company_name = :company_name")
    //Boolean isCompanyExists(String company_name);


    //@Query("SELECT c from Client c WHERE c.client_email = :email")
    //Client getClientByEmail(String email);

    //@Query("SELECT c from Client c WHERE c.client_id = :clientId")
    //Client getClientByClientId(String clientId);
}
