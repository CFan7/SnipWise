package com.snipwise.pojo;

public class ClientLoginResponseDTO
{
    public String client_id;
    public String jwt;
    public String expiration_time;

    public ClientLoginResponseDTO(String client_id, String jwt, String expiration_time)
    {
        this.client_id = client_id;
        this.jwt = jwt;
        this.expiration_time = expiration_time;
    }
}