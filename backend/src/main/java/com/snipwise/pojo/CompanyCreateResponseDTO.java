package com.snipwise.pojo;

public class CompanyCreateResponseDTO
{
    public String company_id;

    public String company_name;

    public String company_subscription_type;

    public String company_subscription_expiration_time;
    public CompanyCreateResponseDTO(
            String company_id,
            String company_name,
            String company_subscription_type,
            String company_subscription_expiration_time
    )
    {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_subscription_type = company_subscription_type;
        this.company_subscription_expiration_time = company_subscription_expiration_time;
    }
}
