package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.pojo.Company;
import com.snipwise.pojo.CompanyCreateDTO;
import com.snipwise.pojo.CompanyCreateResponseDTO;
import com.snipwise.repository.CompanyRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CompanyServiceImpl implements CompanyService
{
    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Override
    public void addGroupToCompany(String company_name, String group_name)
    {
        companyRepository.addGroupToCompany(company_name,group_name);
    }

    public CompanyCreateResponseDTO createCompany(String jwtString, CompanyCreateDTO companyCreateDTO)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw an exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String client_email = jwt.subject;
        if(companyRepository.isCompanyExists(companyCreateDTO.company_name()))
        {
            throw new CompanyAlreadyExistException();
        }
        if(!clientService.isClientExistByEmail(client_email))
        {
            throw new ClientNotExistException();
        }
        ArrayList<String> admins = new ArrayList<>();
        admins.add(client_email);
        ArrayList<String> members = new ArrayList<>();
        members.add(client_email);

        Company company = new Company(
                companyCreateDTO.company_name(),
                "free",
                "na",
                client_email,
                admins,
                members,
                new ArrayList<>()
        );
        companyRepository.createCompany(company);
        clientService.initClientForCompany(client_email,company.company_name());



        /*
        CompanyPermission companyPermission = new CompanyPermission();
        companyPermission.company_id = createdCompany.company_id;
        companyPermission.client_id = client_email;
        companyPermission.permission_type = "x";

        companyPermissionRepository.save(companyPermission);
        */
        return new CompanyCreateResponseDTO(
                company.company_name(),
                company.company_subscription_type(),
                company.company_subscription_expiration_time()
        );
    }

}
