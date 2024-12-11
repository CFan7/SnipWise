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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CompanyServiceImpl implements CompanyService
{
    @Autowired
    private ClientService clientService;
    @Autowired
    private GroupService groupService;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @Override
    public void addMember(String jwtString, String companyName, CompanyAddMemberDTO companyAddMemberDTO)
    {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
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
            client.companyMembers().add(companyName);
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
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw an exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);

        String client_email = jwt.subject;
        if(companyRepository.isCompanyExists(companyCreateDTO.companyName()))
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
                "0",
                companyCreateDTO.companyName(),
                "free",
                ZonedDateTime.parse("9999-12-31T23:59:59Z"),
                client_email,
                admins,
                members,
                new ArrayList<>()

        );
        companyRepository.createCompany(company);
        clientService.initClientForCompany(client_email,company.companyName());

        return new CompanyCreateResponseDTO(
                company.companyName(),
                company.companySubscriptionType(),
                company.companySubscriptionExpirationTime()
        );
    }
    @Override
    public void updateMember(String jwtString, String companyName, String clientEmail, CompanyModifyMemberDTO companyModifyMemberDTO)
    {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
        String clientEmailRequester = jwt.subject;
        String role = companyModifyMemberDTO.role();
        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.isClientExist(clientEmailRequester))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientMemberOfCompany(clientEmail,companyName))//client has to first be a member of the company
        {
            throw new IllegalArgumentException("client has to first be a member of the company");
        }
        Client client = clientService.getClient(clientEmail);
        if (clientService.hasClientOwnerOfCompany(clientEmailRequester,companyName))
        {
            switch (role)
            {
                case "owner":
                    company.setOwner(clientEmail);
                    if(!company.admins().contains(clientEmail))
                    {
                        company.admins().add(clientEmail);
                    }
                    if(!client.companyOwners().contains(companyName))
                    {
                        client.companyOwners().add(companyName);
                    }
                    if(!client.companyAdmins().contains(companyName))
                    {
                        client.companyAdmins().add(companyName);
                    }
                    break;
                case "admin":
                    if(!company.admins().contains(clientEmail))
                    {
                        company.admins().add(clientEmail);
                    }
                    if(!client.companyAdmins().contains(companyName))
                    {
                        client.companyAdmins().add(companyName);
                    }
                    break;
                case "member":
                    company.admins().remove(clientEmail);
                    client.companyAdmins().remove(companyName);
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
        String clientEmailRequester = jwt.subject;

        Company company = companyRepository.getCompany(companyName);//throw exception if company not exist
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.isClientExist(clientEmailRequester))
        {
            throw new ClientNotExistException();
        }
        if(clientService.hasClientOwnerOfCompany(clientEmailRequester,companyName))
        {
            if (company.owner().equals(clientEmail))//owner can remove admins and members
            {
                throw new IllegalArgumentException("owner cannot be removed");
            }
        }
        else if(clientService.hasClientAdminOfCompany(clientEmailRequester,companyName))
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
        client.companyMembers().remove(companyName);
        client.companyAdmins().remove(companyName);
        client.companyOwners().remove(companyName);

        companyRepository.updateCompany(company);
        clientService.updateClient(client);
    }

    @Override
    public List<CompanyGetGroupResponseDTO> getCompanyGroups(String jwtString, String companyName)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail = jwt.subject;
        if(!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!clientService.hasClientMemberOfCompany(clientEmail,companyName))
        {
            throw new ClientUnauthorizedException();
        }
        Company company = companyRepository.getCompany(companyName);

        List<CompanyGetGroupResponseDTO> groups = new ArrayList<>();
        for (String groupId : company.groups())
        {
            Group group = groupService.getGroupByGroupId(groupId);
            if (group.owner().equals(clientEmail))
            {
                groups.add(new CompanyGetGroupResponseDTO(groupId,group.groupName(),"owner"));
            }
            else if (group.admins().contains(clientEmail))
            {
                groups.add(new CompanyGetGroupResponseDTO(groupId,group.groupName(),"admin"));
            }
            else if (group.writeMembers().contains(clientEmail))
            {
                groups.add(new CompanyGetGroupResponseDTO(groupId,group.groupName(),"write_member"));
            }
            else if (group.members().contains(clientEmail))
            {
                groups.add(new CompanyGetGroupResponseDTO(groupId,group.groupName(),"member"));
            }
            else
            {
                ;
            }

        }
        return groups;
    }
}