package com.snipwise.controller;

import com.snipwise.exception.*;
import com.snipwise.pojo.*;
import com.snipwise.service.URLService;
import io.fusionauth.jwt.JWTExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping
public class URLController
{
    @Autowired
    private URLService urlService;

    @GetMapping("/s/{companyName}/{suffix}")
    public RedirectView getURLRequest(@PathVariable String companyName, @PathVariable String suffix)
    {
        try
        {
            String queryUrl = companyName+".snip-wise.com/s/"+suffix;
            String query_result = urlService.getOriginalURL(queryUrl);
            return new RedirectView(query_result);
        }
        catch (URLRecordNotExistException | URLRecordNotActivatedException e)
        {
            return new RedirectView("https://www.example.com");
        }
    }
    @PostMapping("/api/url")
    public ResponseEntity<URLCreateResponseDTO> createURLRecord(
            @RequestHeader("Authorization") String jwtString,
            @RequestBody URLCreateDTO entity)
    {
        try
        {
            URLCreateResponseDTO urlCreateResponseDTO = urlService.createURLRecord(jwtString,entity);
            return ResponseEntity.status(HttpStatus.OK).body(urlCreateResponseDTO);
        }
        catch(io.fusionauth.jwt.JWTExpiredException e)
        {
            // Expires
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (ClientNotExistException |GroupNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (URLRecordAlreadyExistException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/api/url")
    public ResponseEntity<URL> deleteURLRecord(@RequestHeader("Authorization") String jwtString,
                                               @RequestParam("short_url") String short_url)
    {
        try
        {
            urlService.deleteURLRecord(jwtString,short_url);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        catch(JWTExpiredException | ClientUnauthorizedException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (URLRecordNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (ClientNotExistException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}