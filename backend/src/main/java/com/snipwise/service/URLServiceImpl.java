package com.snipwise.service;

import com.snipwise.exception.*;
import com.snipwise.pojo.*;
import com.snipwise.repository.BigtableRepository;
import com.snipwise.repository.ClientRepository;
import com.snipwise.repository.GroupRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class URLServiceImpl implements URLService
{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BigtableRepository bigtableRepository;

    @Autowired
    GroupService groupService;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Override
    public String getURLRequest(String shortURL)
    {
        URL query_result = bigtableRepository.getRecordByShortURL(shortURL);
        if (query_result == null)
        {
            throw new URLRecordNotExistException();
        }
        else if (query_result.isDeleted())
        {
            throw new URLRecordDeletedException();
        }
        else if (!query_result.isActivated())
        {
            throw new URLRecordNotActivatedException();
        }
        else
        {
            return query_result.original_url();
        }
    }

    @Override
    public void createURLRecord(String jwtString, URLCreateDTO entity)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientId = jwt.subject;
        Client client = clientRepository.getClientByClientId(clientId);

        if (client == null)
        {
            throw new ClientNotExistException();
        }

        Group group = groupService.getGroupByGroupId(entity.group_id);
        if (group == null)
        {
            throw new GroupNotExistException();
        }
        String permission = groupService.getRelationBetweenClientAndGroup(clientId,entity.group_id);
        if (permission == null|| !(permission.equals("w") || permission.equals("x")))
        {
            throw new ClientUnauthorizedException();
        }
        if(getURLRequest(entity.short_url) != null)
        {
            throw new URLRecordAlreadyExistException();
        }

        URL urlEntity = new URL(
                entity.short_url,
                entity.original_url,
                entity.expiration_time,
                Boolean.FALSE,
                entity.isActivated,
                entity.group_id,
                clientId
        );

        bigtableRepository.save(urlEntity);
        URLCreateResponseDTO urlCreateResponseDTO =new URLCreateResponseDTO(
                entity.short_url,
                entity.original_url,
                entity.expiration_time,
                Boolean.FALSE,
                entity.isActivated,
                entity.group_id,
                clientId

        );
    }
    @Override
    public void deleteURLRecord(String jwtString, String short_url)
    {
        String jwtString_pure = jwtString.substring(7);
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientId = jwt.subject;
        Client client = clientRepository.getClientByClientId(clientId);
        if (client == null)
        {
            throw new ClientNotExistException();
        }

        URL urlEntity = bigtableRepository.getRecordByShortURL(short_url);
        if (urlEntity == null)
        {
            throw new URLRecordNotExistException();
        }


        String permission = groupService.getRelationBetweenClientAndGroup(clientId,urlEntity.group_id());
        if (permission == null|| !(permission.equals("w") || permission.equals("x")))
        {
            throw new ClientUnauthorizedException();
        }

        bigtableRepository.delete(short_url);
    }
}
