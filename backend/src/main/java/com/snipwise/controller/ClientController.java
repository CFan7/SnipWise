package com.snipwise.controller;


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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/clients")
public class ClientController {



    @Autowired
    private ClientRepository clientRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @PostMapping()
    public ResponseEntity<Client> createClient(@RequestBody ClientCreateDTO client_create_dto)
    {
        Client client = new Client();
        client.client_id=null;
        client.client_name = client_create_dto.client_name;
        client.client_email = client_create_dto.client_email;
        long count = clientRepository.count();
        if (count == 0)
        {
            client.passwd_encrypted = BCrypt.hashpw(client_create_dto.passwd,BCrypt.gensalt());
            Client savedUser = clientRepository.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponseDTO> login(@RequestBody ClientLoginDTO client_login_dto)
    {
        Client client = clientRepository.getClientByEmail(client_login_dto.client_email);
        if (client == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else
        {
            if(BCrypt.checkpw(client_login_dto.passwd,client.passwd_encrypted))
            {


                Signer signer = HMACSigner.newSHA256Signer(JWT_SECRET);
                ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
                JWT jwt = new JWT().setIssuer("snip-wise.com")
                        .setSubject(client.client_id)
                        .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5));
                String encodedJWT = JWT.getEncoder().encode(jwt, signer);

                ClientLoginResponseDTO clientLoginResponseDTO = new ClientLoginResponseDTO(client.client_id, encodedJWT,zonedDateTime.toString()
                );

                return ResponseEntity.status(HttpStatus.OK).body(clientLoginResponseDTO);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
    }
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientGetResponseDTO> getClient(@RequestHeader("Authorization") String jwtString, @PathVariable("clientId") String clientId)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        try
        {
            JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
            if (!Objects.equals(jwt.subject, clientId))
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            else
            {
                Client client = clientRepository.getClientByClientId(clientId);
                if (client == null)
                {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                } else
                {
                    ClientGetResponseDTO clientGetDTO = new ClientGetResponseDTO(client.client_id, client.client_name, client.client_email);
                    return ResponseEntity.status(HttpStatus.OK).body(clientGetDTO);
                }
            }

        }
        catch(io.fusionauth.jwt.JWTExpiredException e)
        {
            // Expires
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }
}
//@GetMapping
    //public List<User> getAllUsers() {
    //    return userRepository.findAll();
    //}

    //@GetMapping("/{id}")
    //public
    //ResponseEntity<User> getUserById(@PathVariable Long id) {
    //    Optional<User> user = userRepository.findById(id);
    //    return user.map(ResponseEntity::ok)
    //            .or
    //    ElseGet(() -> ResponseEntity.notFound().build());
    //}

    /*@PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUse  
        r = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public
    eEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().
        build();
    }*/
