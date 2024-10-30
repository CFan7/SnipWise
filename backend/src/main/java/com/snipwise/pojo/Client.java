package com.snipwise.pojo;


import jakarta.persistence.*;

@Entity
@Table(name="client_info")
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    public String client_id;

    @Column(name = "name")
    public String client_name;
    @Column(name = "passwd_encrypted")
    public String passwd_encrypted;
    @Column(name = "email")
    public String client_email;
}
