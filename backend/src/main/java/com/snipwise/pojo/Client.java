package com.snipwise.pojo;


import com.google.cloud.bigtable.data.v2.models.Row;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Client(
        String version,
        String clientName,
        String passwdEncrypted,
        String clientEmail,
        List<String> companyOwners,
        List<String> companyAdmins,
        List<String> companyMembers,
        List<String> groupOwners,
        List<String> groupAdmins,
        List<String> groupWriteMembers,
        List<String> groupMembers,
        LocalDate dateOfBirth,
        String phoneNumber
)
{
    private static ArrayList<String> getArrayList(String[] arr)
    {
        if (arr.length == 1 && arr[0].isEmpty())
        {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(arr));
    }
    public Client(String clientEmail, Row row)
    {

        this(
                row.getCells("default","version").get(0).getValue().toStringUtf8(),
                row.getCells("default","clientName").get(0).getValue().toStringUtf8(),
                row.getCells("default","passwdEncrypted").get(0).getValue().toStringUtf8(),
                clientEmail,
                getArrayList(row.getCells("default","companyOwners").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","companyAdmins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","companyMembers").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","groupOwners").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","groupAdmins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","groupWriteMembers").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","groupMembers").get(0).getValue().toStringUtf8().split(";;")),
                LocalDate.parse(row.getCells("default","dateOfBirth").get(0).getValue().toStringUtf8()),
                row.getCells("default","phoneNumber").get(0).getValue().toStringUtf8()

        );
    }
}
