package com.snipwise.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ClientGetResponseDTO(
        String clientName,
        String clientEmail,
        LocalDate dateOfBirth,
        String phoneNumber
) {
    public ClientGetResponseDTO(Client client)
    {
        this(
                client.clientName(),
                client.clientEmail(),
                client.dateOfBirth(),
                client.phoneNumber()
        );
    }
}