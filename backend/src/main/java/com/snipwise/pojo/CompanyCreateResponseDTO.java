package com.snipwise.pojo;

import java.time.ZonedDateTime;

public record CompanyCreateResponseDTO
        (
                String companyName,
                String companySubscriptionType,
                ZonedDateTime companySubscriptionExpirationTime
        )
{
}
