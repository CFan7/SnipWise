package com.snipwise.repository;

import com.snipwise.pojo.URL;

import java.util.List;

public interface URLRepository
{
    void createURLRecord(URL entity);
    URL getRecordByShortURL(String short_url);
    void deleteURLRecord(String short_url);

    List<URL> getGroupURLs(String groupId);
}
