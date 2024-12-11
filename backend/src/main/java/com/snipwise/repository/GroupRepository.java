package com.snipwise.repository;

import com.snipwise.pojo.Group;
import com.snipwise.pojo.URL;

import java.util.List;

public interface GroupRepository
{
    void createGroup(Group group);


    Boolean hasGroupExists(String groupId);
    Group getGroup(String groupId);
}
