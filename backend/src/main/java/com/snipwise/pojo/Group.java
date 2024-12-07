package com.snipwise.pojo;
import com.google.cloud.bigtable.data.v2.models.Row;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Group
        (
                String version,
                String group_id,
                String group_name,
                String company_name,
                String owner,
                List<String> admins,
                List<String> write_members,
                List<String> members

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
    public Group(String groupId,Row row)
    {
        this(row.getCells("default","version").get(0).getValue().toStringUtf8(),
                groupId,
                row.getCells("default","group_name").get(0).getValue().toStringUtf8(),
                row.getCells("default","company_name").get(0).getValue().toStringUtf8(),
                row.getCells("default","owner").get(0).getValue().toStringUtf8(),
                getArrayList(row.getCells("default","admins").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","write_members").get(0).getValue().toStringUtf8().split(";;")),
                getArrayList(row.getCells("default","members").get(0).getValue().toStringUtf8().split(";;"))

        );

    }

}
