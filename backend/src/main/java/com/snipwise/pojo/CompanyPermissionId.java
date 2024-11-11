package com.snipwise.pojo;

import java.io.Serializable;
import java.util.Objects;

public class CompanyPermissionId implements Serializable
{
    private String client_id;
    private String company_id;

    public CompanyPermissionId() {}

    public CompanyPermissionId(String client_id, String company_id) {
        this.client_id = client_id;
        this.company_id = company_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPermissionId that = (CompanyPermissionId) o;
        return Objects.equals(client_id, that.client_id) && Objects.equals(company_id, that.company_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client_id, company_id);
    }
}