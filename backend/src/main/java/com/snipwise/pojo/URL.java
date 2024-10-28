package com.snipwise.pojo;

public record URL(String short_url,String original_url, Long expiration_time, Boolean isDeleted, Boolean isActivated, String group_id, String creator_api_key) {
}
