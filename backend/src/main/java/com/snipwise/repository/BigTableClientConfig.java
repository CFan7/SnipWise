package com.snipwise.repository;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BigTableClientConfig
{
    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.bigtable.instance-id}")
    private String instanceId;
    @Bean
    public BigtableDataClient bigtable()
    {

        BigtableDataSettings settings = BigtableDataSettings.newBuilder()
                .setProjectId(projectId)
                .setInstanceId(instanceId)
                .build();

        try
        {
            return BigtableDataClient.create(settings);
        }
        catch (NotFoundException e)
        {
            System.err.println("Failed to read from a non-existent table: " + e.getMessage());
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {
            System.out.println("Error during quickstart: \n" + e.toString());
            throw new RuntimeException(e);
        }
    }
}
