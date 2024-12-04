package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
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
    public void addMember(String jwtString, String company_name, String role)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail = jwt.subject;

        Company company = companyRepository.getCompany(company_name);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientAdminOfCompany(clientEmail,company_name))
        {
            throw new ClientUnauthorizedException();
        }

        switch (role)
        {
            case "admin":
                company.admins().add(clientEmail);
                companyRepository.updateCompany(company);
                break;
            case "member":
                company.members().add(clientEmail);
                companyRepository.updateCompany(company);
                break;
            default:
                throw new IllegalArgumentException("role must be either admin or member");
        }
    }
    @Override
    public void addGroupToCompany(String companyName, String groupId)
    {
        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if (company.groups().contains(groupId))
        {
            return;
        }
        company.groups().add(groupId);
        companyRepository.updateCompany(company);
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
        if(!clientService.isClientExist(client_email))
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
                new ArrayList<>(),
                "0"
        );
        companyRepository.createCompany(company);
        clientService.initClientForCompany(client_email,company.company_name());

        return new CompanyCreateResponseDTO(
                company.company_name(),
                company.company_subscription_type(),
                company.company_subscription_expiration_time()
        );
    }

}
