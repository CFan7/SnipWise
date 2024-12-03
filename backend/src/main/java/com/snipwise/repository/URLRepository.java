package com.snipwise.repository;

import com.snipwise.pojo.URL;

public interface URLRepository
{

    URL getRecordByShortURL(String short_url);

    void createURLRecord(URL entity);

    void deleteURLRecord(String short_url);
}
