package com.snipwise.pojo;

import java.util.ArrayList;
import java.util.List;

public record CompanyCreateResponseDTO
        (
                String company_name,
                String company_subscription_type,
                String company_subscription_expiration_time
        )
{
}
