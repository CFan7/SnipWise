package com.snipwise.service;

import com.snipwise.pojo.URL;
import com.snipwise.pojo.URLCreateDTO;
import com.snipwise.pojo.URLCreateResponseDTO;

public interface URLService
{
    String getOriginalURL(String shortURL);

    URL getURLRecord(String shortURL);

    URLCreateResponseDTO createURLRecord(String jwtString, URLCreateDTO entity);

    void deleteURLRecord(String jwtString, String short_url);
}
