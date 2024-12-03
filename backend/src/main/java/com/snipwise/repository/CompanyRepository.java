package com.snipwise.repository;

import com.snipwise.pojo.Company;

public interface CompanyRepository
{
    Boolean isCompanyExists(String company_name);

    void addGroupToCompany(String company_name, String group_name);

    void createCompany(Company company);
    void updateCompany(Company company);
}
