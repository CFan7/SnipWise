package com.snipwise.controller;

import com.snipwise.pojo.URL;
import com.snipwise.repository.BigtableRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
public class URLController
{
    @Autowired
    private BigtableRepository bigtableRepository;

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
    @PostMapping("/url_management")
    public int createURLRecord(@RequestBody URL entity)
    {
        try
        {
            bigtableRepository.save(entity);
            return 200;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 400;
        }


        /*
        client.client_id = null;
        client.client_name = entity.client_name;
        client.client_email = entity.client_email;
        client.passwd_encrypted = entity.passwd;
        Client savedUser = entity.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);*/
    }



}
