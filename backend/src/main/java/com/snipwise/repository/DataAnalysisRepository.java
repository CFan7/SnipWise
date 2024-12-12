package com.snipwise.repository;

import com.snipwise.pojo.DataAnalysisRespondDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface DataAnalysisRepository
{


    @Async
    void setRow(String shortUrl, String ipAddr);

    List<DataAnalysisRespondDTO> readData(String shortUrl);
}
