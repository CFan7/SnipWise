package com.snipwise.pojo;

import java.time.ZonedDateTime;

/*public record URLCreateDTO
{
    public String suffix;
    public String originalUrl;
    public Long expirationTimeUnix;
    public Boolean isActivated;
    public String groupId;

    public URLCreateDTO(String suffix,String originalUrl,
                        String expiration_time,
    Boolean isActivated,
    String groupId)
    {
        this.suffix = suffix;
        this.originalUrl = originalUrl;
        this.expiration_time = expiration_time;
        this.isActivated = isActivated;
        this.groupId = groupId;
    }
}

 */
public record URLCreateDTO(
        String suffix,
        String originalUrl,
        ZonedDateTime expirationTime,
        Boolean isActivated,
        String groupId
)
{
}