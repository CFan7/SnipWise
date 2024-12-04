package com.snipwise.pojo;


import com.google.cloud.bigtable.data.v2.models.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Client(
        String version,
        String client_name,
        String passwd_encrypted,
        String client_email,
        List<String> company_owners,
        List<String> company_admins,
        List<String> company_members,
        List<String> group_owners,
        List<String> group_admins,
        List<String> group_write_members,
        List<String> group_members

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
                row.getCells("default","passwd_encrypted").get(0).getValue().toStringUtf8(),
                clientEmail,
                getArrayList(row.getCells("default","company_owners").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","company_admins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","company_members").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","group_owners").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","group_admins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","group_write_members").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","group_members").get(0).getValue().toStringUtf8().split(";;"))

        );
    }
}
