package com.snipwise.pojo;

import jakarta.persistence.*;

@Entity
@Table(name="company_permissions")
@IdClass(CompanyPermissionId.class)
public class CompanyPermission
{
    @Id
    @Column(name = "client_id")
    public String client_id;

    @Id
    @Column(name = "company_id")
    public String company_id;

    @Column(name = "permission_type")
    public String permission_type;
}
