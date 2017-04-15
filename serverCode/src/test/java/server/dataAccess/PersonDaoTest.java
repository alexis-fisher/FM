package server.dataAccess;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.model.Person;

import static org.junit.Assert.assertTrue;


/**
 * Created by Alyx on 3/16/17.
 */
public class PersonDaoTest {
    Database db = new Database();
    PersonDao pd = new PersonDao(db);

    @Before
    public void setUp() throws Exception {
        // Open transaction
        db.openTransaction();

        // Ensure persons table is empty
        pd.deleteAllPersons();

    }

    @After
    public void tearDown() throws Exception {
        // Close transaction
        db.closeTransaction(true);

    }

    @Test
    public void testCreatePerson() throws Exception {
        // Create the person
        pd.createPerson("id_placeholder", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");

        // Make sure the person is in the DB
        Person[] persons = pd.read();
        for (Person p : persons) {
            assertTrue(p.getPersonID().equals("id_placeholder"));
        }
    }



    @Test
    public void testRead() throws Exception {
        // Create person
        pd.createPerson("id_placeholder", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");
        pd.createPerson("id_placeholder1", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");

        // Read persons from DB and assert that they are what was created (they were read correctly)
        Person[] persons = pd.read();
        assertTrue (persons != null);

        // Since we don't know what order the DB will read them out in, we just have to ensure that both were entered
        // The DB will not take duplicates, so this is a valid way to test that both were entered & read.
        assertTrue(persons[0].getPersonID().equals("id_placeholder") || persons[0].getPersonID().equals("id_placeholder1") );
        assertTrue(persons[1].getPersonID().equals("id_placeholder1") || persons[1].getPersonID().equals("id_placeholder") );


    }

    @Test
    public void testDeletePerson() throws Exception {
        // put someone in the persons table
        pd.createPerson("id_placeholder", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");

        // read data out of persons table - there should be something there
        Person[] persons = pd.read();

        // ensure that create added something
        assertTrue(persons.length != 0);

        // delete it from the table
        pd.deletePerson("id_placeholder");

        // read data out of the persons table - there should be nothing there now
        Person[] persons1 = pd.read();

        // if anything in persons table, throw exception
        assertTrue(persons1.length == 0 || persons1 == null);
    }

    @Test
    public void testDeleteAllPersons() throws Exception {
        // put stuff in the persons table
        pd.createPerson("id_placeholder", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");
        pd.createPerson("id_placeholder1", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");

        // read data out of persons table - there should be something there
        Person[] persons = pd.read();

        // ensure that create added something
        assertTrue(persons.length != 0);

        // clear the persons table
        pd.deleteAllPersons();

        // read data out of the persons table - there should be nothing there now
        Person[] persons1 = pd.read();

        // if anything in persons table, throw exception
        assertTrue(persons1.length == 0 || persons1 == null);
    }

}