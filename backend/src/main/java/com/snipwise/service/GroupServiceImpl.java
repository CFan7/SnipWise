package com.snipwise.service;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;
import com.snipwise.pojo.GroupPermission;
import com.snipwise.repository.GroupPermissionRepository;
import com.snipwise.repository.GroupRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    GroupPermissionRepository groupPermissionRepository;

    @Override
    public Group getGroupByGroupId(String groupId)
    {
        return groupRepository.getGroupById(groupId);
    }
    @Override
    public String getRelationBetweenClientAndGroup(String client_id, String group_id)
    {
        return groupPermissionRepository.getPermissionTypeByGroupId(group_id,client_id);
    }

    @Override
    @Transactional
    public Group createGroup(String jwtString, GroupCreateDTO groupCreateDTO, String companyId)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientId = jwt.subject;
        if (!clientService.isClientExistById(clientId))
        {
            throw new ClientNotExistException();
        }

        String permission = companyService.getRelationBetweenClientAndCompany(clientId, companyId);
        if (permission == null || !(permission.equals("w") || permission.equals("x")))
        {
            throw new ClientUnauthorizedException();
        }
        Group group = new Group();
        group.group_name = groupCreateDTO.group_name;
        group.company_id = companyId;


        Group savedGroup = groupRepository.save(group);

        GroupPermission groupPermission = new GroupPermission();
        groupPermission.group_id = savedGroup.group_id;
        groupPermission.client_id = clientId;
        groupPermission.permission_type = "x";

        groupPermissionRepository.save(groupPermission);

        return savedGroup;
    }

}
