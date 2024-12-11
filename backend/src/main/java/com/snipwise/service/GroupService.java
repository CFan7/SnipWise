package com.snipwise.service;

import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;
import com.snipwise.pojo.URL;

import java.util.List;

public interface GroupService
{

    Boolean hasGroupExists(String groupId);
    Group createGroup(String jwtString, GroupCreateDTO groupCreateDTO, String companyName);

    Group getGroupByGroupId(String groupId);

    List<URL> getGroupURLs(String jwtString, String groupId);
}
