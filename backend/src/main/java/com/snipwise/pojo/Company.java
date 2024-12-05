package com.snipwise.pojo;

import com.google.cloud.bigtable.data.v2.models.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
public record Company
(
    String company_name,
    String company_subscription_type,
    String company_subscription_expiration_time,
    String owner,
    List<String> admins,
    List<String> members,
    List<String> groups,
    String version
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
                getArrayList(row.getCells("default","groups").get(0).getValue().toStringUtf8().split(";;")),
                row.getCells("default","version").get(0).getValue().toStringUtf8()
        );
    }

}
*/
//rewrite it to a class
public class Company
{
    private String company_name;
    private String company_subscription_type;
    private String company_subscription_expiration_time;
    private String owner;
    private List<String> admins;
    private List<String> members;
    private List<String> groups;
    private String version;

    private static ArrayList<String> getArrayList(String[] arr)
    {
        if (arr.length == 1 && arr[0].isEmpty())
        {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(arr));
    }
    public Company(String company_name, String company_subscription_type, String company_subscription_expiration_time, String owner, List<String> admins, List<String> members, List<String> groups, String version)
    {
        this.company_name = company_name;
        this.company_subscription_type = company_subscription_type;
        this.company_subscription_expiration_time = company_subscription_expiration_time;
        this.owner = owner;
        this.admins = admins;
        this.members = members;
        this.groups = groups;
        this.version = version;
    }
    public Company(Row row)
    {
        this.company_name = row.getCells("default","company_name").get(0).getValue().toStringUtf8();
        this.company_subscription_type = row.getCells("default","company_subscription_type").get(0).getValue().toStringUtf8();
        this.company_subscription_expiration_time = row.getCells("default","company_subscription_expiration_time").get(0).getValue().toStringUtf8();
        this.owner = row.getCells("default","owner").get(0).getValue().toStringUtf8();
        this.admins = getArrayList(row.getCells("default","admins").get(0).getValue().toStringUtf8().split(";;"));
        this.members = getArrayList(row.getCells("default","members").get(0).getValue().toStringUtf8().split(";;"));
        this.groups = getArrayList(row.getCells("default","groups").get(0).getValue().toStringUtf8().split(";;"));
        this.version = row.getCells("default","version").get(0).getValue().toStringUtf8();
    }

    public String company_name() {
        return company_name;
    }

    public String company_subscription_type() {
        return company_subscription_type;
    }

    public String company_subscription_expiration_time() {
        return company_subscription_expiration_time;
    }

    public String owner() {
        return owner;
    }
    //setters owner
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> admins() {
        return admins;
    }

    public List<String> members() {
        return members;
    }

    public List<String> groups() {
        return groups;
    }

    public String version() {
        return version;
    }
}