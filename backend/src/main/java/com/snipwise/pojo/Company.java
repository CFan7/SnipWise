package com.snipwise.pojo;

import com.google.cloud.bigtable.data.v2.models.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Company
(
    String company_name,
    String company_subscription_type,
    String company_subscription_expiration_time,
    String owner,
    List<String> admins,
    List<String> members,
    List<String> groups
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
    public Company(Row row)
    {
        this( row.getCells("default","company_name").get(0).getValue().toStringUtf8(),
                row.getCells("default","company_subscription_type").get(0).getValue().toStringUtf8(),
                row.getCells("default","company_subscription_expiration_time").get(0).getValue().toStringUtf8(),
                row.getCells("default","owner").get(0).getValue().toStringUtf8(),
                getArrayList(row.getCells("default","admins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","members").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","groups").get(0).getValue().toStringUtf8().split(";;"))
        );
    }

}
