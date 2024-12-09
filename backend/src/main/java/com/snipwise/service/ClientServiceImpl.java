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
    public Boolean isClientExist(String email)
    {
        return clientRepository.hasClientExists(email);
    }
    @Override
    public Boolean hasClientOwnerOfCompany(String email, String companyName)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.companyOwners().contains(companyName);
    }
    @Override
    public Boolean hasClientAdminOfCompany(String email, String companyName)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.companyAdmins().contains(companyName);
    }
    @Override
    public Boolean hasClientMemberOfCompany(String email, String companyName)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.companyMembers().contains(companyName);
    }
    @Override
    public Boolean hasClientOwnerOfGroup(String email, String groupId)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.groupOwners().contains(groupId);
    }
    @Override
    public Boolean hasClientAdminOfGroup(String email, String groupId)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.groupAdmins().contains(groupId);
    }
    @Override
    public Boolean hasClientWriteMemberOfGroup(String email, String groupId)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.groupWriteMembers().contains(groupId);
    }
    @Override
    public Boolean hasClientMemberOfGroup(String email, String groupId)
    {
        Client client = clientRepository.getClient(email);//raise exception if client not exist
        return client.groupMembers().contains(groupId);
    }

    @Override
    public List<String> getCompanyOwners(String jwtString, String email) {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw an exception here
        JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);

        String clientEmail = jwt.subject;
        if(!clientEmail.equals(email))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.companyOwners();
    }

    @Override
    public List<String> getCompanyAdmins(String jwtString, String email) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw an exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);

        String clientEmail = jwt.subject;
        if(!clientEmail.equals(email))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.companyAdmins();
    }

    @Override
    public List<String> getCompanyMembers(String jwtString, String email) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);

        String clientEmail = jwt.subject;
        if(!clientEmail.equals(email))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.companyMembers();
    }

    @Override
    public List<String> getGroupOwners(String jwtString, String clientEmail) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(clientEmail);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.groupOwners();
    }

    @Override
    public List<String> getGroupAdmins(String jwtString, String clientEmail) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(clientEmail);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.groupAdmins();
    }

    @Override
    public List<String> getGroupWriteMembers(String jwtString, String clientEmail) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(clientEmail);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.groupWriteMembers();
    }
    @Override
    public ClientGetResponseDTO getClient(String email, String jwtString)
    {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        String clientEmailRequester = JWT.getDecoder().decode(jwtStringPure, verifier).subject;
        if(!clientEmailRequester.equals(email))
        {
            throw new ClientUnauthorizedException();
        }
        return new ClientGetResponseDTO(clientRepository.getClient(email));
    }
    @Override
    public Client getClient(String email) {
        return clientRepository.getClient(email);
    }

    @Override
    public void updateClient(Client client) {
        clientRepository.updateClient(client);
    }

    @Override
    public List<String> getGroupMembers(String jwtString, String clientEmail) {
        String jwtStringPure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);

        // If jwt is invalid or has expired, will throw a exception here
        JWT jwt = JWT.getDecoder().decode(jwtStringPure, verifier);
        String clientEmailJwt = jwt.subject;
        if(!clientEmailJwt.equals(clientEmail))
        {
            throw new ClientUnauthorizedException();
        }
        Client client = clientRepository.getClient(clientEmail);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.groupMembers();
    }

    @Override
    public void initClientForGroup(String email, String groupName)
    {
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        client.groupOwners().add(groupName);
        client.groupAdmins().add(groupName);
        client.groupWriteMembers().add(groupName);
        client.groupMembers().add(groupName);
        clientRepository.updateClient(client);
    }

    @Override
    public void initClientForCompany(String email, String companyName)
    {
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        client.companyOwners().add(companyName);
        client.companyAdmins().add(companyName);
        client.companyMembers().add(companyName);
        clientRepository.updateClient(client);
    }
    public Boolean hasClientOwnershipOf(String email,String companyName)
    {
        Client client = clientRepository.getClient(email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        return client.companyOwners().contains(companyName);
    }

    @Override
    public ClientCreateResponseDTO createClient(ClientCreateDTO clientCreateDTO) {

        if (clientRepository.hasClientExists(clientCreateDTO.clientEmail()))
        {
            throw new ClientAlreadyExistException();
        }
        Client client = new Client("0",
                clientCreateDTO.clientName(),
                BCrypt.hashpw(clientCreateDTO.passwd(),BCrypt.gensalt()),
                clientCreateDTO.clientEmail(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                clientCreateDTO.dateOfBirth(),
                clientCreateDTO.phoneNumber()
                );

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
                client.clientName(),
                client.clientEmail()
        );
    }

    @Override
    public ClientLoginResponseDTO login(ClientLoginDTO clientLoginDTO)
    {
        Client client = clientRepository.getClient(clientLoginDTO.clientEmail());
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        if(!BCrypt.checkpw(clientLoginDTO.passwd(),client.passwdEncrypted()))
        {
            throw new ClientNotExistException();
        }
        Signer signer = HMACSigner.newSHA256Signer(JWT_SECRET);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        JWT jwt = new JWT().setIssuer("snip-wise.com")
                .setSubject(client.clientEmail())
                .setExpiration(zonedDateTime);
        String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        return new ClientLoginResponseDTO(
                client.clientEmail(),
                encodedJWT,
                zonedDateTime.toString()
                );
    }
}

