package com.snipwise.repository;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.snipwise.exception.GroupNotExistException;
import com.snipwise.pojo.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupRepositoryImpl implements GroupRepository
{
    @Autowired
    BigtableDataClient bigtableDataClient;
    TableId usersTableID = TableId.of("snip-wise_groups");

    @Override
    public Boolean hasGroupExists(String groupId) {
        Row row = bigtableDataClient.readRow(usersTableID, groupId);
        return row != null;
    }

    @Override
    public Group getGroup(String groupId) {
        Row row = bigtableDataClient.readRow(usersTableID, groupId);
        if (row == null)
        {
            throw new GroupNotExistException();
        }
        return new Group(groupId,row);

    }



    @Override
    public void createGroup(Group group) {
        Row row = bigtableDataClient.readRow(usersTableID, group.groupId());
        String group_members = String.join(";;", group.members());
        String group_admins = String.join(";;", group.admins());
        String group_write_members = String.join(";;", group.writeMembers());

        RowMutation rowMutation = RowMutation.create(usersTableID, group.groupId())
                .setCell("default","version",group.version())
                .setCell("default","groupName",group.groupName())
                .setCell("default","companyName",group.company_name())
                .setCell("default","owner",group.owner())
                .setCell("default","admins",group_admins)
                .setCell("default","writeMembers",group_write_members)
                .setCell("default","members",group_members)
                ;

        bigtableDataClient.mutateRow(rowMutation);

    }

}
