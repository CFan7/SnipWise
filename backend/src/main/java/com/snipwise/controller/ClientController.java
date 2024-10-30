package com.snipwise.controller;


import com.snipwise.pojo.Client;
import com.snipwise.pojo.Client_create_DTO;
import com.snipwise.pojo.Client_login_DTO;
import com.snipwise.repository.ClientRepository;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/client")
public class ClientController {



    @Autowired
    private ClientRepository clientRepository;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @PostMapping("/create_user")
    public ResponseEntity<Client> createUser(@RequestBody Client_create_DTO client_create_dto)
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
    public ResponseEntity<String> login(@RequestBody Client_login_DTO client_login_dto)
    {
        String encrypted_passwd = clientRepository.getEncryptedPassword(client_login_dto.client_email);
        if (encrypted_passwd == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else
        {
            if(BCrypt.checkpw(client_login_dto.passwd,encrypted_passwd))
            {


                Signer signer = HMACSigner.newSHA256Signer(JWT_SECRET);
                JWT jwt = new JWT().setIssuer("snip-wise.com")
                        .setSubject(client_login_dto.client_email)
                        .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5));
                String encodedJWT = JWT.getEncoder().encode(jwt, signer);

                return ResponseEntity.status(HttpStatus.OK).body(encodedJWT);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
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
