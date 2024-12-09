package com.snipwise.pojo;

import com.google.protobuf.ByteString;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Misc
{
    public static ZonedDateTime parseZonedDateTime(ByteString byteString)
    {
        // Long in BigTable

        return Instant.ofEpochSecond(byteString.asReadOnlyByteBuffer().getLong()).atZone(ZoneId.systemDefault());
    }
    public static Boolean parseBoolean(ByteString byteString)
    {
        // Boolean is stored as Long in BigTable as Bytebuffer
        return byteString.asReadOnlyByteBuffer().getLong() == 1;
    }
    public static Long encodeZonedDateTime(ZonedDateTime zonedDateTime)
    {
        // Long in BigTable
        return zonedDateTime.toInstant().getEpochSecond();
    }
}
