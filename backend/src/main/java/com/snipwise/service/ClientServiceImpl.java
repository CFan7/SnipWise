package com.snipwise.service;

import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.*;
import com.snipwise.repository.ClientRepository;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService
{

    @Autowired
    private ClientRepository clientRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Override
    public Boolean isClientExistByEmail(String email)
    {
        return clientRepository.hasClientExists(email);
    }
    @Override
    public Boolean hasClientOwnerOfCompany(String email, String companyName)
    {
        return clientRepository.hasClientOwnerOfCompany(email,companyName);
    }
    @Override
    public Boolean hasClientAdminOfCompany(String email, String companyName)
    {
        return clientRepository.hasClientAdminOfCompany(email,companyName);
    }
    @Override
    public Boolean hasClientOwnerOfGroup(String email, String groupId)
    {
        return clientRepository.hasClientOwnerOfGroup(email,groupId);
    }
    @Override
    public Boolean hasClientAdminOfGroup(String email, String groupId)
    {
        return clientRepository.hasClientAdminOfGroup(email,groupId);
    }


    @Override
    public List<String> getCompanyOwners(String jwtString, String email) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String client_email = jwt.subject;
        if(!client_email.equals(email))
        {
            throw new ClientUnauthorizedException();
        }

        return clientRepository.getCompanyOwners(email);
    }

    @Override
    public List<String> getCompanyAdmins(String jwtString, String email) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String client_email = jwt.subject;
        if(!client_email.equals(email))
        {
            throw new ClientUnauthorizedException();
        }

        return clientRepository.getCompanyAdmins(email);
    }

    @Override
    public List<String> getCompanyMembers(String jwtString, String email) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String client_email = jwt.subject;
        if(!client_email.equals(email))
        {
            throw new ClientUnauthorizedException();
        }

        return clientRepository.getCompanyMembers(email);
    }

    @Override
    public List<String> getGroupOwners(String jwtString, String clientEmail) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        return  clientRepository.getGroupOwners(clientEmail);

    }

    @Override
    public List<String> getGroupAdmins(String jwtString, String clientEmail) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        return  clientRepository.getGroupAdmins(clientEmail);
    }

    @Override
    public List<String> getGroupWriteMembers(String jwtString, String clientEmail) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        return  clientRepository.getGroupWriteMembers(clientEmail);
    }
    @Override
    public List<String> getGroupMembers(String jwtString, String clientEmail) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        return  clientRepository.getGroupMembers(clientEmail);
    }

    @Override
    public void initClientForGroup(String email, String groupName)
    {
        clientRepository.initClientForGroup(email,groupName);
    }

    @Override
    public void initClientForCompany(String email, String companyName)
    {
        clientRepository.initClientForCompany(email,companyName);
    }
    public Boolean hasClientOwnershipOf(String email,String companyName)
    {
        return clientRepository.hasClientOwnerOfCompany(email,companyName);
    }

    @Override
    public ClientCreateResponseDTO createClient(ClientCreateDTO clientCreateDTO) {

        if (clientRepository.hasClientExists(clientCreateDTO.client_email()))
        {
            throw new ClientAlreadyExistException();
        }
        Client client = new Client(
                clientCreateDTO.client_name(),
                BCrypt.hashpw(clientCreateDTO.passwd(),BCrypt.gensalt()),
                clientCreateDTO.client_email(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());

        try
        {
            clientRepository.createClient(client);
        }
        catch (ClientAlreadyExistException e)
        {
            throw new ClientAlreadyExistException();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }


        return new ClientCreateResponseDTO(
                client.client_name(),
                client.client_email()
        );
    }

    @Override
    public ClientLoginResponseDTO login(ClientLoginDTO clientLoginDTO)
    {
        Client client = clientRepository.getClientByEmail(clientLoginDTO.client_email());
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        if(!BCrypt.checkpw(clientLoginDTO.passwd(),client.passwd_encrypted()))
        {
            throw new ClientNotExistException();
        }
        Signer signer = HMACSigner.newSHA256Signer(JWT_SECRET);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        JWT jwt = new JWT().setIssuer("snip-wise.com")
                .setSubject(client.client_email())
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(15));
        String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        return new ClientLoginResponseDTO(
                client.client_email(),
                encodedJWT,
                zonedDateTime.toString()
                );
    }
/*
    @Override
    public ClientGetResponseDTO getClient(String jwtString, String clientId)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
        if (!Objects.equals(jwt.subject, clientId))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClientByClientId(clientId);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return new ClientGetResponseDTO(client.client_id, client.client_name, client.client_email);

    }

 */
}

