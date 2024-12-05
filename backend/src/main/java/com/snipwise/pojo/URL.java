package com.snipwise.pojo;

import com.google.cloud.bigtable.data.v2.models.Row;

public record URL(
        String short_url,
        String original_url,
        Long expiration_time_unix,
        Boolean isActivated,
        String groupId
)
{
    public URL(String short_url, Row row)
    {
        this(   short_url,
                row.getCells("default","original_url").get(0).getValue().toStringUtf8(),
                Long.parseLong(row.getCells("default","expiration_time").get(0).getValue().toStringUtf8()),
                Boolean.parseBoolean(row.getCells("default","isActivated").get(0).getValue().toStringUtf8()),
                row.getCells("default","groupId").get(0).getValue().toStringUtf8()
        );
    }
}
