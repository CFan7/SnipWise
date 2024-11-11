package com.snipwise.pojo;

import jakarta.persistence.*;

@Entity
@Table(name="company_info")
public class Company
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    public String company_id;

    @Column(name = "company_name")
    public String company_name;

    @Column(name = "company_subscription_type")
    public String company_subscription_type;

    @Column(name = "company_subscription_expiration_time")
    public String company_subscription_expiration_time;
}
