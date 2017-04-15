package server.dataAccess;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.model.User;

import static org.junit.Assert.assertTrue;


/**
 * Created by Alyx on 3/18/17.
 */
public class UserDaoTest {
    Database db = new Database();
    UserDao ud = new UserDao(db);

    @Before
    public void setUp() throws Exception {
        // Open transaction
        db.openTransaction();

        // Ensure user table is empty
        ud.deleteAllUsers();

    }

    @After
    public void tearDown() throws Exception {
        // Close transaction
        db.closeTransaction(true);

    }

    @Test
    public void testCreateUser() throws Exception {
        // Create the user 
        ud.createUser("id_placeholder", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");

        // Make sure the user is in the DB
        User[] users = ud.read();
        for (User p : users) {
            assertTrue(p.getUserName().equals("id_placeholder"));
        }
    }

    @Test
    public void testRead() throws Exception {
        // Create user 
        ud.createUser("id_placeholder", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");
        ud.createUser("id_placeholder1", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");

        // Read users from DB and assert that they are what was created (they were read correctly)
        User[] users = ud.read();
        assertTrue (users != null);

        // Since we don't know what order the DB will read them out in, we just have to ensure that both were entered
        // The DB will not take duplicates, so this is a valid way to test that both were entered & read.
        assertTrue(users[0].getUserName().equals("id_placeholder") || users[0].getUserName().equals("id_placeholder1") );
        assertTrue(users[1].getUserName().equals("id_placeholder1") || users[1].getUserName().equals("id_placeholder") );


    }

    @Test
    public void testDeleteUser() throws Exception {
        // put someone in the users table
        ud.createUser("id_placeholder", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");
        // read data out of users table - there should be something there
        User[] users = ud.read();

        // ensure that create added something
        assertTrue(users.length != 0);

        // delete it from the table
        ud.deleteUser("id_placeholder");

        // read data out of the users table - there should be nothing there now
        User[] users1 = ud.read();

        // if anything in users table, throw exception
        assertTrue(users1.length == 0 || users1 == null);
    }

    @Test
    public void testDeleteAllUsers() throws Exception {
        // put stuff in the users table
        ud.createUser("id_placeholder", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");
        ud.createUser("id_placeholder1", "password_placeholder", "email_placeholder",
                "firstName_placeholder", "lastName_placeholder", "gender_placeholder",
                "personID_placeholder");

        // read data out of users table - there should be something there
        User[] users = ud.read();

        // ensure that create added something
        assertTrue(users.length != 0);

        // clear the users table
        ud.deleteAllUsers();

        // read data out of the users table - there should be nothing there now
        User[] users1 = ud.read();

        // if anything in users table, throw exception
        assertTrue(users1.length == 0 || users1 == null);
    }

}