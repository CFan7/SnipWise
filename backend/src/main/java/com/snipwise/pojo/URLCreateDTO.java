package com.snipwise.pojo;

/*public record URLCreateDTO
{
    public String suffix;
    public String original_url;
    public Long expiration_time_unix;
    public Boolean isActivated;
    public String groupId;

    public URLCreateDTO(String suffix,String original_url,
                        String expiration_time,
    Boolean isActivated,
    String groupId)
    {
        this.suffix = suffix;
        this.original_url = original_url;
        this.expiration_time = expiration_time;
        this.isActivated = isActivated;
        this.groupId = groupId;
    }
}

 */
public record URLCreateDTO(
        String suffix,
        String original_url,
        Long expiration_time_unix,
        Boolean isActivated,
        String groupId
)
{
}