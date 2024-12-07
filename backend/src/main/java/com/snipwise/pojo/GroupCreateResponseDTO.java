package com.snipwise.pojo;

public class GroupCreateResponseDTO
{
    public String groupId;
    public String groupName;
    public String companyName;
    public GroupCreateResponseDTO(
            String group_id,
            String group_name,
            String company_name
    )
    {
        this.groupId = group_id;
        this.groupName = group_name;
        this.companyName = company_name;
    }
    public GroupCreateResponseDTO(
            Group group
    )
    {
        this.groupId = group.groupId();
        this.groupName = group.groupName();
        this.companyName = group.company_name();
    }
}
