package server.dataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.print.attribute.standard.DateTimeAtCompleted;

import server.model.AuthToken;
import server.model.User;


/**
 * Created by Alyx on 2/17/17.
 */

public class AuthTokenDao {

    Database db;

    public AuthTokenDao(Database db){
        this.db = db;
    }


    /** Creates an authorization token in the database
     * @param token a generated authorization token
     * @param userName the userName corresponding with some user.
     * */
    public void createToken(String token, String userName, String personID) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String statement = "insert into tokens (personID, authToken, userName) "
            + "values (?, ?, ?);";

            stmt = db.conn.prepareStatement(statement);

            stmt.setString(1, personID);
            stmt.setString(2, token);
            stmt.setString(3, userName);

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("createToken failed: Could not insert token");
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("createToken failed, probably token wasn't unique", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close SQL statement. Uh oh.");
            }

        }
        // THIS IS THE SQL STATEMENT TO CREATE AN AUTH TOKEN IN TABLE

    }

    /** Retrieves all the authorization tokens in the database */
    public AuthToken[] read() throws DatabaseException {
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select * from tokens";
                stmt = db.conn.prepareStatement(sql);

                Set<AuthToken> tokens = new HashSet<>();
                rs = stmt.executeQuery();
                int index = 0;
                while (rs.next()) {
                    AuthToken tok = new AuthToken();
                    tok.setPersonID(rs.getString(1));
                    tok.setToken(rs.getString(2));
                    tok.setUserName(rs.getString(3));
                    tokens.add(tok);
                    index++;
                }
                AuthToken[] tokensInDatabase = new AuthToken[index];
                index = 0;
                for(AuthToken t : tokens){
                    tokensInDatabase[index] = t;
                    index++;
                }

                return tokensInDatabase;
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
            throw new DatabaseException("readTokens failed", e);
        }
        // THIS IS THE SQL STATEMENT TO QUERY FOR ALL TOKENS
    }


    public AuthToken getTokenOwner(String authToken) throws DatabaseException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String getEv = "select * from tokens where authToken = ?";
        try {
            statement = db.conn.prepareStatement(getEv);
            statement.setString(1, authToken);
            rs = statement.executeQuery();
            AuthToken tok = new AuthToken();
            while (rs.next()) {
                tok.setPersonID(rs.getString(1));
                tok.setToken(rs.getString(2));
                tok.setUserName(rs.getString(3));
            }
            return tok;
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


    /** Removes an authorization token from the database
     * @param authToken token to be deleted
     */
    public void deleteToken(String authToken) throws DatabaseException {
        PreparedStatement statement = null;
        String sql = "delete from tokens where authToken = ?";
        try {
            statement = db.conn.prepareStatement(sql);
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't delete authToken");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't close statement");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE AN AUTH TOKEN
    }

    /** Removes all authorization tokens from the database
     */
    public void deleteAllTokens() throws DatabaseException {
        String deleteTok = "delete from tokens";
        PreparedStatement statement = null;
        try {
            statement = db.conn.prepareStatement(deleteTok);
            statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new DatabaseException("Couldn't delete all tokens");
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e){
                throw new DatabaseException("Couldn't execute finally block in delete all tokens");
            }
        }
        // THIS IS THE SQL STATEMENT TO DELETE ALL TOKENS
    }
}
