package com.snipwise.pojo;

public record ClientCreateDTO(
        String client_name,
        String client_email,
        String passwd
)
{
}