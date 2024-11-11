package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.pojo.Company;
import com.snipwise.pojo.CompanyCreateDTO;
import com.snipwise.pojo.CompanyCreateResponseDTO;
import com.snipwise.pojo.CompanyPermission;
import com.snipwise.repository.ClientRepository;
import com.snipwise.repository.CompanyPermissionRepository;
import com.snipwise.repository.CompanyRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService
{
    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @Autowired
    private CompanyPermissionRepository companyPermissionRepository;

    @Override
    public String getRelationBetweenClientAndCompany(String client_id, String company_id)
    {
        return companyPermissionRepository.getPermissionTypeByCompanyId(company_id,client_id);
    }

    @Transactional
    public CompanyCreateResponseDTO createCompany(String jwtString, CompanyCreateDTO companyCreateDTO)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientId = jwt.subject;
        Company company = new Company();
        if(!clientService.isClientExistById(clientId))
        {
            throw new ClientNotExistException();
        }

        if(companyRepository.isCompanyExists(UUID.fromString(companyCreateDTO.company_name)))
        {
            throw new CompanyAlreadyExistException();
        }

        company.company_name = companyCreateDTO.company_name;
        company.company_subscription_type = "free";
        company.company_subscription_expiration_time = "na";
        Company createdCompany = companyRepository.save(company);

        CompanyPermission companyPermission = new CompanyPermission();
        companyPermission.company_id = createdCompany.company_id;
        companyPermission.client_id = clientId;
        companyPermission.permission_type = "x";

        companyPermissionRepository.save(companyPermission);

        return new CompanyCreateResponseDTO(
                createdCompany.company_id,
                createdCompany.company_name,
                createdCompany.company_subscription_type,
                createdCompany.company_subscription_expiration_time
        );
    }
}
