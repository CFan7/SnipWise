package com.snipwise.controller;


import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.*;
import com.snipwise.service.ClientService;

import io.fusionauth.jwt.JWTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping()
    public ResponseEntity<ClientCreateResponseDTO> createClient(@RequestBody ClientCreateDTO client_create_dto)
    {
        try
        {
            ClientCreateResponseDTO client_create_responseDTO = clientService.createClient(client_create_dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(client_create_responseDTO);
        } catch (ClientAlreadyExistException | OptimisticLockingFailureException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ClientLoginResponseDTO> login(@RequestBody ClientLoginDTO client_login_dto)
    {
        try
        {
            ClientLoginResponseDTO clientLoginResponseDTO = clientService.login(client_login_dto);
            return ResponseEntity.status(HttpStatus.OK).body(clientLoginResponseDTO);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientGetResponseDTO> getClient(@RequestHeader("Authorization") String jwtString, @PathVariable("clientId") String clientId)
    {
        try
        {
            ClientGetResponseDTO clientGetResponseDTO = clientService.getClient(jwtString, clientId);
            return ResponseEntity.status(HttpStatus.OK).body(clientGetResponseDTO);
        } catch (JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}