package com.snipwise.pojo;

import com.google.cloud.bigtable.data.v2.models.Row;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
public record Company
(
    String companyName,
    String companySubscriptionType,
    String companySubscriptionExpirationTime,
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
        this( row.getCells("default","companyName").get(0).getValue().toStringUtf8(),
                row.getCells("default","companySubscriptionType").get(0).getValue().toStringUtf8(),
                row.getCells("default","companySubscriptionExpirationTime").get(0).getValue().toStringUtf8(),
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
    private String companyName;
    private String companySubscriptionType;
    private ZonedDateTime companySubscriptionExpirationTime;
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
    public Company(String version,
            String company_name,
                   String company_subscription_type,
                   ZonedDateTime company_subscription_expiration_time,
                   String owner, List<String> admins, List<String> members, List<String> groups)
    {
        this.companyName = company_name;
        this.companySubscriptionType = company_subscription_type;
        this.companySubscriptionExpirationTime = company_subscription_expiration_time;
        this.owner = owner;
        this.admins = admins;
        this.members = members;
        this.groups = groups;
        this.version = version;
    }
    public Company(Row row)
    {
        this.version = row.getCells("default","version").get(0).getValue().toStringUtf8();
        this.companyName = row.getCells("default","companyName").get(0).getValue().toStringUtf8();
        this.companySubscriptionType = row.getCells("default","companySubscriptionType").get(0).getValue().toStringUtf8();
        this.companySubscriptionExpirationTime = Misc.parseZonedDateTime(row.getCells("default","companySubscriptionExpirationTime").get(0).getValue());
        this.owner = row.getCells("default","owner").get(0).getValue().toStringUtf8();
        this.admins = getArrayList(row.getCells("default","admins").get(0).getValue().toStringUtf8().split(";;"));
        this.members = getArrayList(row.getCells("default","members").get(0).getValue().toStringUtf8().split(";;"));
        this.groups = getArrayList(row.getCells("default","groups").get(0).getValue().toStringUtf8().split(";;"));

    }

    public String companyName() {
        return companyName;
    }

    public String companySubscriptionType() {
        return companySubscriptionType;
    }

    public ZonedDateTime companySubscriptionExpirationTime() {
        return companySubscriptionExpirationTime;
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