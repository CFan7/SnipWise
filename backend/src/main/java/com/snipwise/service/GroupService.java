package com.snipwise.service;

import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;

public interface GroupService
{

    Group getGroupByGroupId(String groupId);

    String getRelationBetweenClientAndGroup(String client_id, String group_id);

    Group createGroup(String jwtString, GroupCreateDTO groupCreateDTO, String company_id);
}
