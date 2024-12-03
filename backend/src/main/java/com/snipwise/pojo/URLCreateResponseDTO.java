package com.snipwise.pojo;
/*
public class URLCreateResponseDTO
{
    public String short_url;
    public String original_url;
    public String expiration_time;
    public Boolean isActivated;
    public String groupId;

    public URLCreateResponseDTO(String short_url,
                                String original_url,
                                String expiration_time,
                                Boolean isActivated,
                                String groupId)
    {
        this.short_url = short_url;
        this.original_url = original_url;
        this.expiration_time = expiration_time;
        this.isActivated = isActivated;
        this.groupId = groupId;
    }
}
*/
public record URLCreateResponseDTO(
        String short_url,
        String original_url,
        Long expiration_time_unix,
        Boolean isActivated,
        String group_id
)
{
    public URLCreateResponseDTO( URL url)
    {
        this(   url.short_url(),
                url.original_url(),
                url.expiration_time_unix(),
                url.isActivated(),
                url.groupId()
        );
    }
}