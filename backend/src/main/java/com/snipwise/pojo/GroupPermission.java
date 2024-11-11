package com.snipwise.pojo;

import jakarta.persistence.*;

@Entity
@Table(name="group_permissions")
@IdClass(GroupPermissionId.class)
public class GroupPermission
{
    @Id
    @Column(name = "client_id")
    public String client_id;

    @Id
    @Column(name = "group_id")
    public String group_id;

    @Column(name = "permission_type")
    public String permission_type;
}
