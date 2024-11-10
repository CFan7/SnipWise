package com.snipwise.repository;

import com.snipwise.pojo.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface BigtableRepository {

    URL getRecordByShortURL(String short_url);

    void save(URL entity);

    void delete(String short_url);
}
