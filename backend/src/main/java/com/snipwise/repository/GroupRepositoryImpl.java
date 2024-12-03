package com.snipwise.repository;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.snipwise.exception.GroupNotExistException;
import com.snipwise.pojo.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupRepositoryImpl implements GroupRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    String usersTableID = "snip-wise_groups";

    @Override
    public Boolean hasGroupExists(String groupId) {
        Row row = bigtableDataClient.readRow(usersTableID, groupId);
        return row != null;
    }

    @Override
    public Group getGroupById(String groupId) {
        Row row = bigtableDataClient.readRow(usersTableID, groupId);
        if (row == null)
        {
            throw new GroupNotExistException();
        }
        return new Group(row);

    }



    @Override
    public void createGroup(Group group) {
        Row row = bigtableDataClient.readRow(usersTableID, group.group_id());
        //if (row != null)
        //{
        //    throw new GroupAlreadyExistException();
        //}
        String group_members = String.join(";;", group.members());
        String group_admins = String.join(";;", group.admins());
        String group_write_members = String.join(";;", group.write_members());

        RowMutation rowMutation = RowMutation.create(usersTableID, group.group_id())
                .setCell("default","group_name",group.group_name())
                .setCell("default","company_id",group.company_name())
                .setCell("default","admins",group_admins)
                .setCell("default","write_members",group_write_members)
                .setCell("default","members",group_members);

        bigtableDataClient.mutateRow(rowMutation);

    }

}
