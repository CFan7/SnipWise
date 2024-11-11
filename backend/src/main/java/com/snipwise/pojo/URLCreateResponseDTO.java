package com.snipwise.pojo;

public class URLCreateResponseDTO
{
    public String short_url;
    public String original_url;
    public Long expiration_time;
    public Boolean isActivated;
    public String group_id;
    public String creator_id;

    public URLCreateResponseDTO(String short_url, String original_url,
                                Long expiration_time,
                                Boolean isDeleted,
                                Boolean isActivated,
                                String group_id,
                                String creator_id)
    {
        this.short_url = short_url;
        this.original_url = original_url;
        this.expiration_time = expiration_time;
        this.isActivated = isActivated;
        this.group_id = group_id;
        this.creator_id = creator_id;

    }
}
