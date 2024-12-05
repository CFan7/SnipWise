package com.snipwise.repository;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.snipwise.exception.CompanyAlreadyExistException;
import com.snipwise.exception.CompanyNotExistException;
import com.snipwise.exception.OptimisticLockException;
import com.snipwise.pojo.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyRepositoryImpl implements CompanyRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    TableId usersTableID = TableId.of("snip-wise_companies");

    @Override
    public Boolean isCompanyExists(String company_name) {
        Row row = bigtableDataClient.readRow(usersTableID, company_name);
        return row != null;
    }

    @Override
    public Company getCompany(String companyName) {
        Row row = bigtableDataClient.readRow(usersTableID, companyName);
        if (row == null)
        {
            throw new CompanyNotExistException();
        }
        return new Company(row);
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
                .setCell("default","groups",String.join(";;", company.groups()))
                .setCell("default", "version", company.version());

        bigtableDataClient.mutateRow(rowMutation);

    }

    @Override
    public void updateCompany(Company company)
    {
        Long currentVersion = Long.parseLong(company.version());
        String company_admins = String.join(";;", company.admins());
        String company_members = String.join(";;", company.members());
        Filters.Filter filter = Filters.FILTERS.chain()
                .filter(Filters.FILTERS.qualifier().exactMatch("version"))
                .filter(Filters.FILTERS.value().range().startClosed(String.valueOf(currentVersion)).endOpen(String.valueOf(currentVersion + 1)));
        // Optimistic Locking
        ConditionalRowMutation conditionalRowMutation =
                ConditionalRowMutation.create(usersTableID, company.company_name())
                        .condition(filter)
                        .then(
                                Mutation.create()
                                        .setCell("default","company_name",company.company_name())
                                        .setCell("default","company_subscription_type",company.company_subscription_type())
                                        .setCell("default","company_subscription_expiration_time",company.company_subscription_expiration_time())
                                        .setCell("default","owner",company.owner())
                                        .setCell("default","admins",company_admins)
                                        .setCell("default","members",company_members)
                                        .setCell("default","groups",String.join(";;", company.groups()))
                                        .setCell("default", "version", String.valueOf(currentVersion + 1))
                        );

        boolean mutationApplied = bigtableDataClient.checkAndMutateRow(conditionalRowMutation);
        if (!mutationApplied) {
            throw new OptimisticLockException();
        }
    }
}
