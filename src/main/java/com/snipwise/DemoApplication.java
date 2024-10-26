package com.snipwise;

import com.snipwise.pojo.Client;
import com.snipwise.pojo.Client_DTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping({"/{short_url}"})
    public RedirectView hello(@PathVariable String short_url)
    {
        RedirectView redirectView = new RedirectView();

        redirectView.setUrl("https://"+short_url+".www.example.com");
        return redirectView;
    }

}
