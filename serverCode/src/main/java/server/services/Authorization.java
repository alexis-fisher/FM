package server.services;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.AuthToken;

/**
 * Created by Alyx on 2/17/17.
 */

public class Authorization {

    Database db = new Database();

    /**
     * Checks to see if a token is in the database
     *
     * @param authToken token to be checked
     * @return TRUE if the token is in the database, FALSE otherwise
     * @throws DatabaseException
     */
    public boolean authenticate(String authToken) throws DatabaseException {
        try {
            db.openTransaction();
            AuthToken[] tokens = db.tokens.read();
            db.closeTransaction(true);
            if (tokens != null && tokens.length > 0) {
                for (AuthToken t : tokens) {
                    if (t.getToken().equals(authToken)) {
                        return true;
                    }
                }
            } else {
                // if there are no tokens, just return false (no error message)
                return false;
            }
        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
        return false;
    }

    /**
     * Gets the username of the User the auth token belongs to
     *
     * @param authToken token to check
     * @return username
     * @throws DatabaseException
     */
    public String getTokenOwner(String authToken) throws DatabaseException {
        try {
            db.openTransaction();
            AuthToken token = db.tokens.getTokenOwner(authToken);
            db.closeTransaction(true);
            return token.getUserName();

        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}

