package com.snipwise.pojo;

import jakarta.persistence.*;

@Entity
@Table(name="group_info")
public class Group
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    public String group_id;

    @Column(name = "group_name")
    public String group_name;

    @Column(name = "company_id")
    public String company_id;
}
