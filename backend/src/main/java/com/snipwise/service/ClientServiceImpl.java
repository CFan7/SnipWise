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
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService
{

    @Autowired
    private ClientRepository clientRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Override
    public Boolean isClientExistById(String clientId)
    {
        Client client = clientRepository.getClientByClientId(clientId);
        return !Objects.isNull(client);
    }
    @Override
    public Boolean isClientExistByEmail(String email)
    {
        return clientRepository.isClientExists(email);
    }

    @Override
    public ClientCreateResponseDTO createClient(ClientCreateDTO clientCreateDTO) {

        if (clientRepository.isClientExists(clientCreateDTO.client_email))
        {
            throw new ClientAlreadyExistException();
        }
        Client client = new Client();
        client.client_name = clientCreateDTO.client_name;
        client.client_email = clientCreateDTO.client_email;
        client.passwd_encrypted = BCrypt.hashpw(clientCreateDTO.passwd,BCrypt.gensalt());
        Client savedUser = clientRepository.save(client);

        ClientCreateResponseDTO responseDTO = new ClientCreateResponseDTO();
        responseDTO.client_id = savedUser.client_id;
        responseDTO.client_email = savedUser.client_email;
        responseDTO.client_name = savedUser.client_name;
        return responseDTO;
    }

    @Override
    public ClientLoginResponseDTO login(ClientLoginDTO clientLoginDTO)
    {
        Client client = clientRepository.getClientByEmail(clientLoginDTO.client_email);
        if (client == null)
        {
            throw new ClientNotExistException();
        }
        if(!BCrypt.checkpw(clientLoginDTO.passwd,client.passwd_encrypted))
        {
            throw new ClientNotExistException();
        }
        Signer signer = HMACSigner.newSHA256Signer(JWT_SECRET);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        JWT jwt = new JWT().setIssuer("snip-wise.com")
                .setSubject(client.client_id)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(15));
        String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        return new ClientLoginResponseDTO(
                client.client_id,
                encodedJWT,
                zonedDateTime.toString()
                );
    }

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
}

