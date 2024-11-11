package com.snipwise.service;

import com.snipwise.pojo.*;
import org.springframework.web.bind.annotation.RequestBody;


public interface ClientService
{
    Boolean isClientExistById(String clientId);

    Boolean isClientExistByEmail(String email);

    ClientCreateResponseDTO createClient(ClientCreateDTO client_create_dto);

    ClientLoginResponseDTO login(@RequestBody ClientLoginDTO client_login_dto);

    ClientGetResponseDTO getClient(String jwtString, String clientId);
}
