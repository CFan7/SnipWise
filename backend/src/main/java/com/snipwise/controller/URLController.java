package com.snipwise.controller;

import com.snipwise.pojo.*;
import com.snipwise.repository.BigtableRepository;
import com.snipwise.repository.ClientRepository;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@RestController
@RequestMapping
public class URLController
{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BigtableRepository bigtableRepository;
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @GetMapping("/s/**")
    public RedirectView getURLRequest(HttpServletRequest request)
    {
        System.out.println(request);
        String fullUrl = request.getRequestURL().toString();
        System.out.println(fullUrl);
        URL query_result = bigtableRepository.getRecordByShortURL(fullUrl);
        if (query_result == null)
        {
            return new RedirectView("https://www.example.com");
        } else if (query_result.isDeleted())
        {
            return new RedirectView("https://www.example.com");
        } else if (!query_result.isActivated())
        {
            return new RedirectView("https://www.example.com");
        } else
        {
            return new RedirectView(query_result.original_url());
        }
    }
    @PostMapping("/api/url")
    public ResponseEntity<URL> createURLRecord(@RequestHeader("Authorization") String jwtString,@RequestBody URLCreateDTO entity)
    {
        String jwtString_pure = jwtString.substring(7);// remove "Bearer "
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        try
        {
            JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
            String clientId = jwt.subject;
            Client client = clientRepository.getClientByClientId(clientId);
            if (client == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            else
            {
                try
                {
                    URL urlEntity = new URL(
                            entity.short_url,
                            entity.original_url,
                            entity.expiration_time,
                            Boolean.FALSE,
                            entity.isActivated,
                            entity.group_id,
                            clientId
                            );

                    bigtableRepository.save(urlEntity);
                    return ResponseEntity.status(HttpStatus.CREATED).body(urlEntity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

    @DeleteMapping("/api/url")
    public ResponseEntity<URL> deleteURLRecord(@RequestHeader("Authorization") String jwtString,
                                               @RequestParam("short_url") String short_url)
    {
        String jwtString_pure = jwtString.substring(7);
        Verifier verifier = HMACVerifier.newVerifier(JWT_SECRET);
        try
        {
            JWT jwt = JWT.getDecoder().decode(jwtString_pure, verifier);
            String clientId = jwt.subject;
            Client client = clientRepository.getClientByClientId(clientId);
            if (client == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else
            {
                URL urlEntity = bigtableRepository.getRecordByShortURL(short_url);
                if (!urlEntity.creator_id().equals(clientId))
                {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                } else
                {
                    try
                    {
                        bigtableRepository.delete(short_url);
                        return ResponseEntity.status(HttpStatus.OK).body(null);
                    } catch (Exception e)
                    {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                }
            }
        }
        catch(io.fusionauth.jwt.JWTExpiredException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}