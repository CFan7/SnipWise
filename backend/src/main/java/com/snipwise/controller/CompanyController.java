package com.snipwise.controller;


import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.pojo.*;

import com.snipwise.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController
{
    @Autowired
    private CompanyService companyService;

    @PostMapping()
    public ResponseEntity<CompanyCreateResponseDTO> createCompany(@RequestHeader("Authorization") String jwtString, @RequestBody CompanyCreateDTO companyCreateDTO)
    {
        try
        {
            CompanyCreateResponseDTO companyCreateResponseDTO = companyService.createCompany(jwtString, companyCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(companyCreateResponseDTO);
        }
        //jwt
        catch (io.fusionauth.jwt.JWTException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //company name already exist || fail to write record to the db
        catch (CompanyAlreadyExistException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //other exceptions
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/{companyName}/members/{role}")
    public ResponseEntity<Void> addMember(@RequestHeader("Authorization") String jwtString, @PathVariable String companyName, @PathVariable String role)
    {
        try
        {
            companyService.addMember(jwtString, companyName, role);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        //jwt
        catch (io.fusionauth.jwt.JWTException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //company name already exist || fail to write record to the db
        catch (CompanyAlreadyExistException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //other exceptions
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
