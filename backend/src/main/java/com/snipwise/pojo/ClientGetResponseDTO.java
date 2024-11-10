package com.snipwise.pojo;

public class ClientGetResponseDTO
{
    public String client_id;
    public String client_name;
    public String client_email;
    public ClientGetResponseDTO(String client_id, String client_name, String client_email)
    {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_email = client_email;
    }
}
