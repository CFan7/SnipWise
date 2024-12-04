package com.snipwise.service;

import com.snipwise.pojo.Company;
import com.snipwise.pojo.CompanyCreateDTO;
import com.snipwise.pojo.CompanyCreateResponseDTO;

public interface CompanyService {

    void addMember(String jwtString, String company_name, String role);

    void addGroupToCompany(String company_name, String group_name);

    CompanyCreateResponseDTO createCompany(String jwtString, CompanyCreateDTO companyCreateDTO);
}

