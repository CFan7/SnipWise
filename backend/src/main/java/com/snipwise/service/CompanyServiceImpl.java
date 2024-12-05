package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.pojo.*;
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
    public void addMember(String jwtString, String companyName, CompanyAddMemberDTO companyAddMemberDTO)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail_requester = jwt.subject;
        String clientEmail = companyAddMemberDTO.email();

        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail_requester))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientAdminOfCompany(clientEmail_requester,companyName))
        {
            throw new ClientUnauthorizedException();
        }
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientMemberOfCompany(clientEmail,companyName))
        {
            company.members().add(clientEmail);
            Client client = clientService.getClient(clientEmail);
            client.company_members().add(companyName);
            clientService.updateClient(client);
            companyRepository.updateCompany(company);
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
    @Override
    public void updateMember(String jwtString, String companyName, String clientEmail, CompanyModifyMemberDTO companyModifyMemberDTO)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail_requester = jwt.subject;
        String role = companyModifyMemberDTO.role();
        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.isClientExist(clientEmail_requester))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientMemberOfCompany(clientEmail,companyName))//client has to first be a member of the company
        {
            throw new IllegalArgumentException("client has to first be a member of the company");
        }
        Client client = clientService.getClient(clientEmail);
        if (clientService.hasClientOwnerOfCompany(clientEmail_requester,companyName))
        {
            switch (role)
            {
                case "owner":
                    company.setOwner(clientEmail);
                    if(!company.admins().contains(clientEmail))
                    {
                        company.admins().add(clientEmail);
                    }
                    if(!client.company_owners().contains(companyName))
                    {
                        client.company_owners().add(companyName);
                    }
                    if(!client.company_admins().contains(companyName))
                    {
                        client.company_admins().add(companyName);
                    }
                    break;
                case "admin":
                    if(!company.admins().contains(clientEmail))
                    {
                        company.admins().add(clientEmail);
                    }
                    if(!client.company_admins().contains(companyName))
                    {
                        client.company_admins().add(companyName);
                    }
                    break;
                case "member":
                    company.admins().remove(clientEmail);
                    client.company_admins().remove(companyName);
                    break;
                default:
                    throw new IllegalArgumentException("role must be either admin or member or owner");
            }
        }
        else
        {
            throw new ClientUnauthorizedException();//admins and members cannot update other members
        }

        companyRepository.updateCompany(company);
        clientService.updateClient(client);

    }

    @Override
    public void deleteMember(String jwtString, String companyName, String clientEmail) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail_requester = jwt.subject;

        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.isClientExist(clientEmail_requester))
        {
            throw new ClientNotExistException();
        }
        if(clientService.hasClientOwnerOfCompany(clientEmail_requester,companyName))
        {
            if (company.owner().equals(clientEmail))//owner can remove admins and members
            {
                throw new IllegalArgumentException("owner cannot be removed");
            }
        }
        else if(clientService.hasClientAdminOfCompany(clientEmail_requester,companyName))
        {
            if (company.admins().contains(clientEmail))///admin can remove members
            {
                throw new ClientUnauthorizedException();
            }
        }
        else//members cannot remove other members
        {
            throw new ClientUnauthorizedException();
        }

        for (String groupId : company.groups())
        {
            if(clientService.hasClientMemberOfGroup(clientEmail,groupId))
            {
                throw new IllegalArgumentException("client is member of group, cannot be removed");
            }
        }
        company.admins().remove(clientEmail);
        company.members().remove(clientEmail);
        Client client = clientService.getClient(clientEmail);
        client.company_members().remove(companyName);
        client.company_admins().remove(companyName);
        client.company_owners().remove(companyName);

        companyRepository.updateCompany(company);
        clientService.updateClient(client);
    }

}
