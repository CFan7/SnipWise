package com.snipwise.repository;

import com.snipwise.pojo.Group;

public interface GroupRepository
{
    void createGroup(Group group);


    Boolean hasGroupExists(String groupId);
    Group getGroup(String groupId);


}
