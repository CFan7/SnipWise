package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;
import com.snipwise.repository.GroupRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService
{
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Autowired
    ClientService clientService;

    @Autowired
    CompanyService companyService;

    @Autowired
    GroupRepository groupRepository;

    @Override
    public Boolean hasGroupExists(String groupId)
    {
        return groupRepository.hasGroupExists(groupId);
    }

    @Override
    public Group getGroupByGroupId(String groupId)
    {
        return groupRepository.getGroup(groupId);
    }

    @Override
    public Group createGroup(String jwtString, GroupCreateDTO groupCreateDTO, String companyName)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientEmail = jwt.subject;
        if (!clientService.isClientExist(clientEmail))
        {
            throw new ClientNotExistException();
        }


        if (!clientService.hasClientAdminOfCompany(clientEmail, companyName))
        {
            throw new ClientUnauthorizedException();
        }
        String uuid = UUID.randomUUID().toString();
        while(groupRepository.hasGroupExists(uuid))
        {
            uuid = UUID.randomUUID().toString();
        }

        ArrayList<String> admins = new ArrayList<>();
        admins.add(clientEmail);
        ArrayList<String> write_members = new ArrayList<>();
        write_members.add(clientEmail);
        ArrayList<String> members = new ArrayList<>();
        members.add(clientEmail);

        Group group = new Group("0",
                uuid,
                groupCreateDTO.groupName,
                companyName,
                clientEmail,
                admins,
                write_members,
                members

        );
        groupRepository.createGroup(group);
        companyService.addGroupToCompany(companyName, uuid);
        clientService.initClientForGroup(clientEmail, uuid);
        return group;
    }

}
