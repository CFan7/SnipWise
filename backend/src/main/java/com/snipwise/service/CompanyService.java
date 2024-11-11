package com.snipwise.service;

import com.snipwise.pojo.CompanyCreateDTO;
import com.snipwise.pojo.CompanyCreateResponseDTO;

public interface CompanyService {
    String getRelationBetweenClientAndCompany(String client_id, String company_id);

    CompanyCreateResponseDTO createCompany(String jwtString, CompanyCreateDTO companyCreateDTO);
}

