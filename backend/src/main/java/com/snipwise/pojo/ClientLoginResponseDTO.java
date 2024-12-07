package com.snipwise.pojo;

public record ClientLoginResponseDTO(
        String clientEmail,
        String jwt,
        String expirationTime
)
{
}