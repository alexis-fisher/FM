package server.dataAccess;

import java.sql.*;
import java.util.*;

import server.model.Person;

/**
 * Created by Alyx on 2/17/17.
 */

public class PersonDao {

    Database db;

    public PersonDao(Database db){
        this.db = db;
    }


    /** Creates a person in the database with given data, if personID is null,
     *  creates a person with generated data
     *
     * @param personID Unique identifier for this person
     * @param descendant User to which this person belongs
     * @param firstName Person’s first name (non-empty string)
     * @param lastName Person’s last name (non-empty string)
     * @param gender Person’s gender (Male or Female)
     * @param father Person’s father (possibly null)
     * @param mother Person’s mother (possibly null)
     * @param spouse Person’s spouse (possibly null)
     */
    public void createPerson(String personID, String descendant,
                             String firstName, String lastName,
                             String gender, String father,
                             String mother, String spouse) throws DatabaseException {
        if(personID == null){
            personID = UUID.randomUUID().toString();
        }
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "insert into persons (descendant, personID, firstName, " +
                        "lastName, gender, father, mother, spouse) values (?, ?, ?, ?, ?, ?, ?, ?);";
                stmt = db.conn.prepareStatement(sql);

                stmt.setString(1, descendant);
                stmt.setString(2, personID);
                stmt.setString(3, firstName);
                stmt.setString(4, lastName);
                stmt.setString(5, gender);
                stmt.setString(6, father);
                stmt.setString(7, mother);
                stmt.setString(8, spouse);

                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("createPerson failed: Could not insert person");
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("createPerson failed", e);
        }

    }

    /** Reads all person data from the database
     *
     * @return collection of Persons
     */
    public Person[] read() throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select * from persons";
                stmt = db.conn.prepareStatement(sql);

                Set<Person> persons = new HashSet<>();
                rs = stmt.executeQuery();
                int index = 0;
                while (rs.next()) {

                    Person pers = new Person();
                    pers.setDescendant(rs.getString(1));
                    pers.setPersonID(rs.getString(2));
                    pers.setFirstName(rs.getString(3));
                    pers.setLastName(rs.getString(4));
                    pers.setGender(rs.getString(5));
                    pers.setFather(rs.getString(6));
                    pers.setMother(rs.getString(7));
                    pers.setSpouse(rs.getString(8));
                    persons.add(pers);
                    index++;
                }
                Person[] personsInDatabase = new Person[index];
                index = 0;
                for(Person p : persons){
                    personsInDatabase[index] = p;
                    index++;
                }

                return personsInDatabase;
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("readPersons failed", e);
        }
    }

    /** Updates a person in the database
     *
     * @param personID identifier to choose which person to update
     * @param person person object containing the updates
     *
     * */
    public void updatePerson(String personID, Person person) throws DatabaseException {
        // UPDATE A PERSON: delete & recreate
        deletePerson(personID);
        createPerson(person.getPersonID(),person.getDescendant(), person.getFirstName(),
                    person.getLastName(), person.getGender(), person.getFather(),
                    person.getMother(), person.getSpouse());
    }

    public Person getPersonFromID(String personID) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getPers = "select * from persons where personID = ?";
        try {
            statement = db.conn.prepareStatement(getPers);
            statement.setString(1, personID);
            rs = statement.executeQuery();
            Person pers = new Person();
            while (rs.next()) {
                pers.setDescendant(rs.getString(1));
                pers.setPersonID(rs.getString(2));
                pers.setFirstName(rs.getString(3));
                pers.setLastName(rs.getString(4));
                pers.setGender(rs.getString(5));
                pers.setFather(rs.getString(6));
                pers.setMother(rs.getString(7));
                pers.setSpouse(rs.getString(8));
            }
            return pers;
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't get event");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement");
            }
        }
    }

    public void dropPersonsbyUser(String username) throws DatabaseException {
        PreparedStatement statement = null;
        String deletePerson = "delete from persons where descendant = ?";
        try {
            statement = db.conn.prepareStatement(deletePerson);
            statement.setString(1, username);
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Couldn't delete!");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            }
            catch (SQLException e){
                throw new DatabaseException("Couldn't close delete statement....");
            }
        }

        // THIS IS THE SQL STATEMENT TO DELETE A PERSON

    }

    /** Removes a person from the database
     *
     * @param personID identifier to choose which person to remove
     */
    public void deletePerson(String personID) throws DatabaseException {
        PreparedStatement statement = null;
        String deletePerson = "delete from persons where personID = ?";
        try {
            statement = db.conn.prepareStatement(deletePerson);
            statement.setString(1, personID);
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Couldn't delete!");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            }
            catch (SQLException e){
                throw new DatabaseException("Couldn't close delete statement....");
            }
        }

        // THIS IS THE SQL STATEMENT TO DELETE A PERSON

    }

    /** Removes all persons from the database
     *
     */
    public void deleteAllPersons() throws DatabaseException {
        String deletePerson = "delete from persons";
        PreparedStatement statement = null;
        try {
            statement = db.conn.prepareStatement(deletePerson);
            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't delete!");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            }
            catch (SQLException e){
                throw new DatabaseException("Couldn't close delete statement....");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE ALL PEOPLE

    }

    public Person[] getAllPersonsof(String userName) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getPers = "select * from persons where descendant = ?";
        try {
            statement = db.conn.prepareStatement(getPers);
            statement.setString(1, userName);
            rs = statement.executeQuery();


            Set<Person> persons = new HashSet<>();
            int index = 0;
            while (rs.next()) {
                Person pers = new Person();
                pers.setDescendant(rs.getString(1));
                pers.setPersonID(rs.getString(2));
                pers.setFirstName(rs.getString(3));
                pers.setLastName(rs.getString(4));
                pers.setGender(rs.getString(5));
                pers.setFather(rs.getString(6));
                pers.setMother(rs.getString(7));
                pers.setSpouse(rs.getString(8));
                persons.add(pers);
                index++;
            }
            Person[] personsFromUser = new Person[index];
            index = 0;
            for(Person p : persons){
                personsFromUser[index] = p;
                index++;
            }

            return personsFromUser;

        } catch (SQLException e) {
            throw new DatabaseException("Couldn't find person by user!");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement!");
            }
        }
    }


    public static void main(String[] args) {
        try {
            Database db = new Database();
            db.openTransaction();

            PersonDao pd = new PersonDao(db);
            try {
                pd.deleteAllPersons();
            } catch (DatabaseException e){
                System.out.println("Exception! Couldn't delete.");
                return;
            }
            pd.createPerson("id_placeholder", "descendant_placeholder", "FirstName_placeholder", "LastName_placeholder", "Gender_placeholder", "father_placeholder", "mother_placeholder", "spouse_placeholder");
            Person[] persons = pd.read();
            if(persons != null) {
                for (Person p : persons) {
                    if(p == null){
                        System.out.println("null person");
                    }
                    else {
                        System.out.println(p.getPersonID());
                    }
                }

                System.out.println("OK");
            }
            else{
                System.out.println("Didn't write.");
                throw new DatabaseException("Couldn't read any persons");
            }
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}