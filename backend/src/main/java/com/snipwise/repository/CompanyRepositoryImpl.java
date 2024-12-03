package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.exception.CompanyNotExistException;
import com.snipwise.pojo.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.cloud.bigtable.data.v2.models.Row;
@Component
public class CompanyRepositoryImpl implements CompanyRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    String usersTableID = "snip-wise_companies";

    @Override
    public Boolean isCompanyExists(String company_name) {
        Row row = bigtableDataClient.readRow(usersTableID, company_name);
        return row != null;
    }

    public void createCompany(Company company)
    {

        Row row = bigtableDataClient.readRow(usersTableID, company.company_name());
        if (row != null)
        {
            throw new CompanyAlreadyExistException();

        }
        String company_admins = String.join(";;", company.admins());
        String company_members = String.join(";;", company.members());

        RowMutation rowMutation = RowMutation.create(usersTableID, company.company_name())
                .setCell("default","company_name",company.company_name())
                .setCell("default","company_subscription_type",company.company_subscription_type())
                .setCell("default","company_subscription_expiration_time",company.company_subscription_expiration_time())
                .setCell("default","owner",company.owner())
                .setCell("default","admins",company_admins)
                .setCell("default","members",company_members)
                .setCell("default","groups",String.join(";;", company.groups()));

        bigtableDataClient.mutateRow(rowMutation);

    }

    @Override
    public void addGroupToCompany(String company_name, String groupId) {
        Row row = bigtableDataClient.readRow(usersTableID, company_name);
        if (row == null)
        {
            throw new CompanyNotExistException();
        }
        Company company = new Company(row);
        company.groups().add(groupId);
        updateCompany(company);

    }

    @Override
    public void updateCompany(Company company) {
        String company_admins = String.join(";;", company.admins());
        String company_members = String.join(";;", company.members());
        RowMutation rowMutation = RowMutation.create(usersTableID, company.company_name())
                .setCell("default","company_name",company.company_name())
                .setCell("default","company_subscription_type",company.company_subscription_type())
                .setCell("default","company_subscription_expiration_time",company.company_subscription_expiration_time())
                .setCell("default","owner",company.owner())
                .setCell("default","admins",company_admins)
                .setCell("default","members",company_members)
                .setCell("default","groups",String.join(";;", company.groups()));

        bigtableDataClient.mutateRow(rowMutation);

    }
}
