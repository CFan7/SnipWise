package com.snipwise.controller;


import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
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
    @PostMapping("/{companyName}/members")
    public ResponseEntity<Void> addMember(
            @RequestHeader("Authorization") String jwtString,
            @PathVariable String companyName,
            @RequestBody CompanyAddMemberDTO companyAddMemberDTO
            )
    {
        try
        {
            companyService.addMember(jwtString, companyName, companyAddMemberDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        //jwt
        catch (io.fusionauth.jwt.JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //company name already exist || fail to write record to the db
        catch (ClientNotExistException| IllegalArgumentException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //other exceptions
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{companyName}/members/{clientEmail}")
    public ResponseEntity<String> updateMember(
            @RequestHeader("Authorization") String jwtString,
            @PathVariable String companyName,
            @PathVariable String clientEmail,
            @RequestBody CompanyModifyMemberDTO companyModifyMemberDTO)
    {
        try
        {
            companyService.updateMember(jwtString, companyName,clientEmail, companyModifyMemberDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        //jwt
        catch (io.fusionauth.jwt.JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //company name already exist || fail to write record to the db
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (IllegalArgumentException e)
        {
            //return info about the error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        //other exceptions
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{companyName}/members/{clientEmail}")
    public ResponseEntity<String> deleteMember(
            @RequestHeader("Authorization") String jwtString,
            @PathVariable String companyName,
            @PathVariable String clientEmail)
    {
        try
        {
            companyService.deleteMember(jwtString, companyName,clientEmail);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        //jwt
        catch (io.fusionauth.jwt.JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //company name already exist || fail to write record to the db
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (IllegalArgumentException e)
        {
            //return info about the error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        //other exceptions
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
