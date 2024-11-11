package com.snipwise.pojo;

public class GroupCreateResponseDTO
{
    public String group_id;
    public String group_name;
    public String company_id;
    public GroupCreateResponseDTO(
            String group_id,
            String group_name,
            String company_id
    )
    {
        this.group_id = group_id;
        this.group_name = group_name;
        this.company_id = company_id;
    }
    public GroupCreateResponseDTO(
            Group group
    )
    {
        this.group_id = group.group_id;
        this.group_name = group.group_name;
        this.company_id = group.company_id;
    }
}
