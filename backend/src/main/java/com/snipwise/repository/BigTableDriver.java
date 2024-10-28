package com.snipwise.repository;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/*
public class BigTableDriver {
}


package com.comp413.bigtabledemo;

import com.comp413.bigtabledemo.obj.DemoUser;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
*/
/**
 * This class emulates the client interacting with the DB.
 */
/*
public class BigTableDriver {

    //Timestamp formatter
    private static final DateTimeFormatter TSFORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {

        // Establish and maintain connection with our Bigtable instance
        String projectId = "rice-comp-539-spring-2022";
        String instanceId = "comp-539-bigtable";

        BigTableManager bt = new BigTableManager(projectId, instanceId,"snipwise-customers");

        System.out.println("Create 3 users");
        bt.createUser(new DemoUser("anthony413", "orange", timestamp()));
        bt.createUser(new DemoUser("rohith413", "blue", timestamp()));
        bt.createUser(new DemoUser("alexei413", "red", timestamp()));

        System.out.println("Alexei's Color: " + bt.getUserColor("alexei413"));

        System.out.println("\nChange Alexei's Color");
        bt.updateColor("alexei413", "yellow");
        bt.updateColor("alexei539", "yellow");
        System.out.println("Alexei's Color: " + bt.getUserColor("alexei413"));

        System.out.println("alexei413 color: " + bt.getUserColor("alexei413"));
        System.out.println("alexei539 color: " + bt.getUserColor("alexei539"));

        bt.createUser(new DemoUser("alexei539", "red", timestamp()));
        System.out.println("\nCurrent 'User' column family (table)");
        for (DemoUser u: bt.getUsers()) {
            System.out.println(u);
        }

        System.out.println("\nDeleting alexei413:");
        bt.deleteUser("alexei413");
        for (DemoUser u: bt.getUsers()) {
            System.out.println(u);
        }

        System.out.println("\nDeleting all users now:");
        for (DemoUser u: bt.getUsers()) {
            System.out.println("Deleting " + u.username());
            bt.deleteUser(u.username());
        }

        System.out.println("\nAnything left in the table?");
        for (DemoUser u: bt.getUsers()) {
            System.out.println(u);
        }
        System.out.println("\nNope!");

        bt.close();
    }


    private static String timestamp() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(TSFORMATTER);
    }
}
*/