package com.snipwise.service;

import com.snipwise.pojo.DataAnalysisRespondDTO;
import com.snipwise.pojo.URL;
import com.snipwise.pojo.URLCreateDTO;
import com.snipwise.pojo.URLCreateResponseDTO;

import java.util.List;

public interface URLService
{

    String getOriginalURL(String shortURL, String ipAddr);

    URL getURLRecord(String shortURL);

    URLCreateResponseDTO createURLRecord(String jwtString, URLCreateDTO entity);

    List<DataAnalysisRespondDTO> getURLData(String jwtString, String short_url);

    void deleteURLRecord(String jwtString, String short_url);
}
