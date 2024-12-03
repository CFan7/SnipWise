package com.snipwise.service;

import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;

public interface GroupService
{

    Boolean hasGroupExists(String groupId);

    Group getGroupByGroupId(String groupId);

    Group createGroup(String jwtString, GroupCreateDTO groupCreateDTO, String companyName);
}
