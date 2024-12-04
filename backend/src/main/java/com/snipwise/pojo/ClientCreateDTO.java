package com.snipwise.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ClientCreateDTO(
        String clientName,
        String clientEmail,
        String passwd,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate  DateOfBirth,
        String PhoneNumber
)
{
}