package com.snipwise.service;

import com.snipwise.pojo.*;

import java.util.List;

public interface CompanyService {


    void addMember(String jwtString, String companyName, CompanyAddMemberDTO companyAddMemberDTO);

    void addGroupToCompany(String company_name, String group_name);

    CompanyCreateResponseDTO createCompany(String jwtString, CompanyCreateDTO companyCreateDTO);

    void updateMember(String jwtString, String companyName, String clientEmail, CompanyModifyMemberDTO companyModifyMemberDTO);

    void deleteMember(String jwtString, String companyName, String clientEmail);

    List<CompanyGetGroupResponseDTO> getCompanyGroups(String jwtString, String companyName);
}

