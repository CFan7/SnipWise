package com.snipwise.pojo;

import jakarta.persistence.*;

public class CompanyCreateDTO
{
    @Column(name = "company_name")
    public String company_name;
}
