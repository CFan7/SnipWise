package com.snipwise.pojo;

import java.io.Serializable;
import java.util.Objects;

public class GroupPermissionId implements Serializable
{
    private String client_id;
    private String group_id;

    public GroupPermissionId() {}

    public GroupPermissionId(String client_id, String group_id) {
        this.client_id = client_id;
        this.group_id = group_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupPermissionId that = (GroupPermissionId) o;
        return Objects.equals(client_id, that.client_id) && Objects.equals(group_id, that.group_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client_id, group_id);
    }
}