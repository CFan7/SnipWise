package com.snipwise.controller;


import com.snipwise.pojo.Client;
import com.snipwise.pojo.Client_DTO;
import com.snipwise.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client_management")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<Client> createUser(@RequestBody Client_DTO client_dto)
    {
        Client client = new Client();
        client.client_id=null;
        client.client_name = client_dto.client_name;
        client.client_email = client_dto.client_email;
        client.passwd_encrypted = client_dto.passwd;
        Client savedUser = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
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
