package com.snipwise.controller;

import com.snipwise.exception.ClientNotExistException;
import com.snipwise.exception.ClientUnauthorizedException;
import com.snipwise.pojo.Group;
import com.snipwise.pojo.GroupCreateDTO;
import com.snipwise.pojo.GroupCreateResponseDTO;
import com.snipwise.service.CompanyService;
import com.snipwise.service.CompanyServiceImpl;
import com.snipwise.service.GroupService;
import io.fusionauth.jwt.JWTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/companies/{companyName}/groups")
public class GroupController
{
    @Autowired
    private GroupService groupService;
    @Autowired
    private CompanyService companyService;

    @PostMapping()
    public ResponseEntity<GroupCreateResponseDTO> createGroup(@RequestHeader("Authorization") String jwtString, @PathVariable String companyName, @RequestBody GroupCreateDTO groupCreateDTO)
    {
        try
        {
            Group group = groupService.createGroup(jwtString,groupCreateDTO,companyName);
            return ResponseEntity.status(HttpStatus.CREATED).body(new GroupCreateResponseDTO(group));
        }
        catch (JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    @GetMapping()
    public ResponseEntity<List<List<String>>> getGroups(@RequestHeader("Authorization") String jwtString, @PathVariable String companyName)
    {
        try
        {
            List<List<String>> result = companyService.getCompanyGroups(jwtString,companyName);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        catch (JWTException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
