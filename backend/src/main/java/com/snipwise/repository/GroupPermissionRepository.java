package com.snipwise.repository;

import com.snipwise.pojo.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, UUID> {


    @Query("SELECT gp.permission_type FROM GroupPermission gp where gp.group_id=:group_id AND gp.client_id =:client_id")
    String getPermissionTypeByGroupId(String group_id, String client_id);

    //@Query("SELECT c from Client c WHERE c.client_email = :email")
    //Client getClientByEmail(String email);

    //@Query("SELECT c from Client c WHERE c.client_id = :clientId")
    //Client getClientByClientId(String clientId);
}
