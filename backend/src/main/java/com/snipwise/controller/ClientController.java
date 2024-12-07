package com.snipwise.controller;


import com.snipwise.exception.ClientAlreadyExistException;
import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.*;
import com.snipwise.service.ClientService;

import io.fusionauth.jwt.JWTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
        } catch (ClientAlreadyExistException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{clientEmail}")
    public ResponseEntity<ClientGetResponseDTO> getClient(@PathVariable("clientEmail") String clientEmail, @RequestHeader("Authorization") String jwtString)
    {
        try
        {
            ClientGetResponseDTO clientGetResponseDTO = clientService.getClient(clientEmail, jwtString);
            return ResponseEntity.status(HttpStatus.OK).body(clientGetResponseDTO);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e)
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
    @GetMapping("/{clientEmail}/access")
    public ResponseEntity<List<String>> getClientAccess(
            @RequestHeader("Authorization") String jwtString,
            @PathVariable("clientEmail") String clientEmail,
            @RequestParam("type") String type,
            @RequestParam("role") String role)
    {
        try
        {
            if(type.equals("company"))

            {
                switch (role)
                {
                    case "owner" ->
                    {
                        List<String> clientOwnerCompany = clientService.getCompanyOwners(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientOwnerCompany);
                    }
                    case "admin" ->
                    {
                        List<String> clientAdminCompany = clientService.getCompanyAdmins(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientAdminCompany);
                    }
                    case "member" ->
                    {
                        List<String> clientMemberCompany = clientService.getCompanyMembers(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientMemberCompany);
                    }
                }
            }
            else if(type.equals("group"))
            {
                switch (role)
                {
                    case "owner" ->
                    {
                        List<String> clientOwnerGroup = clientService.getGroupOwners(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientOwnerGroup);
                    }
                    case "admin" ->
                    {
                        List<String> clientAdminGroup = clientService.getGroupAdmins(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientAdminGroup);
                    }
                    case "write_member" ->
                    {
                        List<String> clientWriteMemberGroup = clientService.getGroupWriteMembers(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientWriteMemberGroup);
                    }
                    case "member" ->
                    {
                        List<String> clientMemberGroup = clientService.getGroupMembers(jwtString, clientEmail);
                        return ResponseEntity.status(HttpStatus.OK).body(clientMemberGroup);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}