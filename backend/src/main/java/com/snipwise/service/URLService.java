package com.snipwise.service;

import com.snipwise.pojo.URLCreateDTO;

public interface URLService
{
    String getURLRequest(String shortURL);

    void createURLRecord(String jwtString, URLCreateDTO entity);

    void deleteURLRecord(String jwtString, String short_url);
}
