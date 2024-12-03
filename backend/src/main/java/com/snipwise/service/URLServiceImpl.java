package com.snipwise.service;

import com.snipwise.exception.*;
import com.snipwise.pojo.*;
import com.snipwise.repository.URLRepository;
import com.snipwise.repository.ClientRepository;
import com.snipwise.repository.CompanyRepository;
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
    private ClientService clientService;
    @Autowired
    private URLRepository urlRepository;

    @Autowired
    GroupService groupService;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Override
    public String getOriginalURL(String shortURL)
    {
        try
        {
            URL query_result = urlRepository.getRecordByShortURL(shortURL);
            if (!query_result.isActivated())
            {
                throw new URLRecordNotActivatedException();
            }
            else
            {
                return query_result.original_url();
            }
        }
        catch (URLRecordNotExistException e)
        {
            throw new URLRecordNotExistException();
        }
    }
    @Override
    public URL getURLRecord(String shortURL)
    {
        try
        {
            return urlRepository.getRecordByShortURL(shortURL);
        }
        catch (URLRecordNotExistException e)
        {
            throw new URLRecordNotExistException();
        }
    }


    @Override
    public URLCreateResponseDTO createURLRecord(String jwtString, URLCreateDTO entity)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmail = jwt.subject;

        if (!clientService.isClientExistByEmail(clientEmail))
        {
            throw new ClientNotExistException();
        }
        if(!groupService.hasGroupExists(entity.groupId()))
        {
            throw new GroupNotExistException();
        }
        if (!clientService.hasClientAdminOfGroup(clientEmail, entity.groupId()))
        {
            throw new ClientUnauthorizedException();
        }
        //Group group = groupService.getGroupByGroupId(entity.groupId());
        //String companyName = group.company_name();
        //String short_url = "http://"+companyPrefix+".snip-wise.com/s/" + entity.suffix;
        String short_url = "http://localhost:8080/s/" + entity.suffix();
        try
        {
            getURLRecord(short_url);
            throw new URLRecordAlreadyExistException();//not activated => also exist
        }
        catch(URLRecordNotExistException e)
        {
            // URL record not exist, can create
        }

        URL urlEntity = new URL(
                short_url,
                entity.original_url(),
                entity.expiration_time_unix(),
                entity.isActivated(),
                entity.groupId()
        );

        urlRepository.createURLRecord(urlEntity);

        return new URLCreateResponseDTO(urlEntity);
    }
    @Override
    public void deleteURLRecord(String jwtString, String short_url) throws URLRecordNotExistException, ClientNotExistException, GroupNotExistException, ClientUnauthorizedException
    {
        String jwtString_pure = jwtString.substring(7);
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientId = jwt.subject;
        if (!clientService.isClientExistByEmail(clientId))
        {
            throw new ClientNotExistException();
        }

        try
        {
            URL urlEntity = getURLRecord(short_url);
            String groupId = urlEntity.groupId();
            if (!groupService.hasGroupExists(groupId))
            {
                throw new GroupNotExistException();
            }
            if(!clientService.hasClientAdminOfGroup(clientId,groupId))
            {
                throw new ClientUnauthorizedException();
            }
            urlRepository.deleteURLRecord(short_url);
        }
        catch (URLRecordNotExistException e)
        {
            throw new URLRecordNotExistException();
        }
    }
}
