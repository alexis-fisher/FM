package server.dataAccess;

import java.io.File;
import java.sql.*;

import server.model.AuthToken;

/**
 * Created by Alyx on 3/14/17.
 */

public class Database {

    public PersonDao persons = new PersonDao(this);
    public EventDao events = new EventDao(this);
    public UserDao users = new UserDao(this);
    public AuthTokenDao tokens = new AuthTokenDao(this);

    Connection conn;

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("problem with DRIVER in DATABASE.JAVA");
        }
    }

    public void createTables() throws DatabaseException {
        String userTable = "CREATE TABLE IF NOT EXISTS USERS" +
        "(" +
                "userName varchar(255) not null primary key," +
                "password varchar(255) not null,"+
                "email varchar(255) not null," +
                "firstName varchar(255) not null," +
                "lastName varchar(255) not null," +
                "gender varchar(255) not null," +
                "personID varchar(255) not null" +
        ");";
        String personFields = "CREATE TABLE IF NOT EXISTS PERSONS" +
        "(" +
                "descendant varchar(255) not null, " +
                "personID varchar(255) not null primary key, " +
                "firstName varchar(255) not null, " +
                "lastName varchar(255) not null, " +
                "gender varchar(255) not null, " +
                "father varchar(255), " +
                "mother varchar(255), " +
                "spouse varchar(255)" +

         ");";
        String eventFields = "CREATE TABLE IF NOT EXISTS EVENTS" +
        "(" +
                "descendant varchar(255) not null,"+
                "eventID varchar(255) not null primary key," +
                "personID varchar(255) not null," +
                "latitude varchar(255) not null," +
                "longitude varchar(255) not null," +
                "country varchar(255) not null," +
                "city varchar(255) not null," +
                "eventType varchar(255) not null," +
                "year varchar(255) not null" +

        ");";
        String tokenFields = "CREATE TABLE IF NOT EXISTS TOKENS" + "(" +
                "personID varchar(255) not null," +
                "authToken varchar(255) not null primary key,"+
                "userName varchar(255) not null" +
       ");";

        try {
            PreparedStatement buildUserTable = this.conn.prepareStatement(userTable);
            buildUserTable.executeUpdate();
            PreparedStatement buildPersonTable = this.conn.prepareStatement(personFields);
            buildPersonTable.executeUpdate();
            PreparedStatement buildEventTable = this.conn.prepareStatement(eventFields);
            buildEventTable.executeUpdate();
            PreparedStatement buildTokenTable = this.conn.prepareStatement(tokenFields);
            buildTokenTable.executeUpdate();
        }
        catch (SQLException e){
            throw new DatabaseException("Couldn't build table!");
        }

    }

    public void openTransaction() throws DatabaseException {

        // Connect to database
        File databasePackage = new File("database");
        if(!databasePackage.exists()){
            databasePackage.mkdirs();
        }

        String database = "database" + File.separator + "database.sqlite";
        String url = "jdbc:sqlite:" + database;
        conn = null;

        try {
            conn = DriverManager.getConnection(url);
            createTables();
        } catch (SQLException e){
            throw new DatabaseException("Couldn't get connection!");
        }


        // Set auto commit to false
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("Could not set auto commit on open transaction");

        }
    }

    public void closeTransaction(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            throw new DatabaseException("closeConnection failed", e);
        }

    }


    public void clearTables() throws DatabaseException {
        try {
            String dropUsers = "DROP TABLE IF EXISTS users";
            String dropPersons = "DROP TABLE IF EXISTS persons";
            String dropEvents = "DROP TABLE IF EXISTS events";
            String dropTokens = "DROP TABLE IF EXISTS tokens";

            PreparedStatement clearUses = this.conn.prepareStatement(dropUsers);
            clearUses.executeUpdate();

            PreparedStatement clearPers = this.conn.prepareStatement(dropPersons);
            clearPers.executeUpdate();

            PreparedStatement clearEvs = this.conn.prepareStatement(dropEvents);
            clearEvs.executeUpdate();

            PreparedStatement clearToks = this.conn.prepareStatement(dropTokens);
            clearToks.executeUpdate();


        }
        catch (SQLException e){
            throw new DatabaseException("Couldn't clear tables!");
        }

        String userTable = "CREATE TABLE USERS" +
                "(" +
                "userName varchar(255) not null primary key," +
                "password varchar(255) not null,"+
                "email varchar(255) not null," +
                "firstName varchar(255) not null," +
                "lastName varchar(255) not null," +
                "gender varchar(255) not null," +
                "personID varchar(255) not null" +
                ");";
        String personFields = "CREATE TABLE PERSONS" +
                "(" +
                "descendant varchar(255) not null, " +
                "personID varchar(255) not null primary key, " +
                "firstName varchar(255) not null, " +
                "lastName varchar(255) not null, " +
                "gender varchar(255) not null, " +
                "father varchar(255), " +
                "mother varchar(255), " +
                "spouse varchar(255)" +

                ");";
        String eventFields = "CREATE TABLE EVENTS" +
                "(" +
                "descendant varchar(255) not null,"+
                "eventID varchar(255) not null primary key," +
                "personID varchar(255) not null," +
                "latitude varchar(255) not null," +
                "longitude varchar(255) not null," +
                "country varchar(255) not null," +
                "city varchar(255) not null," +
                "eventType varchar(255) not null," +
                "year varchar(255) not null" +

                ");";
        String tokenFields = "CREATE TABLE TOKENS" + "(" +
                "personID varchar(255) not null," +
                "authToken varchar(255) not null primary key,"+
                "userName varchar(255) not null" +
                ");";
        try {
            PreparedStatement buildUserTable = this.conn.prepareStatement(userTable);
            buildUserTable.executeUpdate();
            PreparedStatement buildPersonTable = this.conn.prepareStatement(personFields);
            buildPersonTable.executeUpdate();
            PreparedStatement buildEventTable = this.conn.prepareStatement(eventFields);
            buildEventTable.executeUpdate();
            PreparedStatement buildTokenTable = this.conn.prepareStatement(tokenFields);
            buildTokenTable.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException("Couldn't rebuild tables!");
        }



    }


}
