package com.snipwise.repository;

import com.snipwise.pojo.Company;

public interface CompanyRepository
{
    void createCompany(Company company);
    void updateCompany(Company company);
    Boolean isCompanyExists(String company_name);
    Company getCompany(String companyName);
}
