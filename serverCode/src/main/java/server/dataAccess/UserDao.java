package server.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import server.model.User;


/**
 * Created by Alyx on 2/17/17.
 */

public class UserDao {

    Database db;

    public UserDao(Database db){
        this.db = db;
    }


    /** Creates a user in the database with given data
     *
     * @param userName Unique user name (non-empty string)
     * @param password User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (Male or Female)
     * @param personID User’s personID (non-empty string)
     */
    public void createUser(String userName, String password,
                             String email, String firstName,
                             String lastName, String gender,
                             String personID) throws DatabaseException {

        try {
            PreparedStatement stmt = null;
            try {
                String sql = "insert into users (userName, password, email, firstName, " +
                        "lastName, gender, personID) values (?, ?, ?, ?, ?, ?, ?);";
                stmt = db.conn.prepareStatement(sql);

                stmt.setString(1, userName);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.setString(4, firstName);
                stmt.setString(5, lastName);
                stmt.setString(6, gender);
                stmt.setString(7, personID);


                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("createUser failed: Could not insert person");
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("createUser failed", e);
        }
        // THIS IS THE SQL STATEMENT TO CREATE A USER
    }

    /** Reads all User data from the database
     *
     * @return collection of Users
     */
    public User[] read() throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select * from users";
                stmt = db.conn.prepareStatement(sql);

                Set<User> users = new HashSet<>();
                rs = stmt.executeQuery();
                int index = 0;
                while (rs.next()) {

                    User user = new User();
                    user.setUsername(rs.getString(1));
                    user.setPassword(rs.getString(2));
                    user.setEmail(rs.getString(3));
                    user.setFirstName(rs.getString(4));
                    user.setLastName(rs.getString(5));
                    user.setGender(rs.getString(6));
                    user.setPersonID(rs.getString(7));
                    users.add(user);
                    index++;
                }
                User[] usersInDatabase = new User[index];
                index = 0;
                for(User u : users){
                    usersInDatabase[index] = u;
                    index++;
                }

                return usersInDatabase;
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
            throw new DatabaseException("readUsers failed", e);
        }
    }

    public User getUserFromName(String userName) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getUser = "select * from users where userName = ?";
        try {
            statement = db.conn.prepareStatement(getUser);
            statement.setString(1, userName);
            rs = statement.executeQuery();

            User user = new User();
            while (rs.next()) {
                user.setUsername(rs.getString(1));
                user.setPassword(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setFirstName(rs.getString(4));
                user.setLastName(rs.getString(5));
                user.setGender(rs.getString(6));
                user.setPersonID(rs.getString(7));
            }
            return user;
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't get user");
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

    /** Removes a User from the database
     *
     * @param userName identifier to choose which User to remove
     * */
    public void deleteUser(String userName) throws SQLException {
        PreparedStatement statement = null;
        String deleteUser = "delete from users where userName = ?";
        try {
            statement = db.conn.prepareStatement(deleteUser);
            statement.setString(1, userName);
            statement.executeUpdate();


        }   catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(statement != null){
                statement.close();
            }
        }

        // THIS IS THE SQL STATEMENT TO DELETE A USER

    }

    /** Removes all Users from the database
     *
     */
    public void deleteAllUsers() throws DatabaseException {
        String deleteUsers = "delete from users";
        PreparedStatement statement = null;
        try {
            statement = db.conn.prepareStatement(deleteUsers);
            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't delete all users");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't execute finally block in delete users");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE ALL USERS

    }
}
