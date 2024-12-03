package com.snipwise.repository;

import com.snipwise.pojo.Group;

public interface GroupRepository {

    Boolean hasGroupExists(String groupId);
    Group getGroupById(String groupId);

    void createGroup(Group group);
}
