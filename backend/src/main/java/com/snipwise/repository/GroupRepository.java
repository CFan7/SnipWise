package com.snipwise.repository;

import com.snipwise.pojo.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {


    @Query("SELECT g from Group g WHERE g.group_id = :groupId")
    Group getGroupById(String groupId);

    //@Query("SELECT c from Client c WHERE c.client_id = :clientId")
    //Client getClientByClientId(String clientId);
}
