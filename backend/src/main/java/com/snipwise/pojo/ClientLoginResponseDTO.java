package com.snipwise.pojo;

public record ClientLoginResponseDTO(
        String client_email,
        String jwt,
        String expiration_time
)
{
}