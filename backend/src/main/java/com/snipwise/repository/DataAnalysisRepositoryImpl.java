package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.snipwise.pojo.DataAnalysisRespondDTO;
import com.snipwise.pojo.Misc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataAnalysisRepositoryImpl implements DataAnalysisRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    TableId usersTableID = TableId.of("snip-wise_da");

    @Async
    @Override
    public void setRow(String shortUrl, String ipAddr)

    {
        Row row = bigtableDataClient.readRow(usersTableID, shortUrl);
        RowMutation mutation = RowMutation.create(
                        usersTableID,
                        shortUrl)
                .setCell("default", "ipAddr", ipAddr);
        bigtableDataClient.mutateRow(mutation);
    }

    @Override
    public List<DataAnalysisRespondDTO> readData(String shortUrl)
    {
        Row row = bigtableDataClient.readRow(usersTableID, shortUrl);
        if (row == null)
        {
            return null;
        }
        else
        {
            //cell has multiple copies with different timestamps
            List<RowCell> ipAddrs = row.getCells("default", "ipAddr");
            List<DataAnalysisRespondDTO> return_ =  new ArrayList<>();
            for (int i = 0; i < ipAddrs.size(); i++)
            {
                return_.add(new DataAnalysisRespondDTO(ipAddrs.get(i).getValue().toStringUtf8(), ipAddrs.get(i).getTimestamp()));
            }
            return return_;

        }
    }
}
