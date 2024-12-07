package com.snipwise.pojo;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.type.DateTime;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import com.snipwise.pojo.Misc;
public record URL(
        String shortUrl,
        String originalUrl,
        ZonedDateTime expirationTime,
        Boolean isActivated,
        String groupId
)
{

    public URL(String shortUrl, Row row)
    {
        this(   shortUrl,
                row.getCells("default","originalUrl").get(0).getValue().toStringUtf8(),
                Misc.parseZonedDateTime(row.getCells("default","expirationTime").get(0).getValue()),
                Misc.parseBoolean(row.getCells("default","isActivated").get(0).getValue()),
                row.getCells("default","groupId").get(0).getValue().toStringUtf8()
        );
    }
}
