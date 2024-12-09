package com.snipwise.pojo;

import java.time.ZonedDateTime;

public record URLCreateResponseDTO(
        String shortUrl,
        String originalUrl,
        ZonedDateTime expirationTime,
        Boolean isActivated,
        String groupId
)
{
    public URLCreateResponseDTO( URL url)
    {
        this(   url.shortUrl(),
                url.originalUrl(),
                url.expirationTime(),
                url.isActivated(),
                url.groupId()
        );
    }
}