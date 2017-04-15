package server.services;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;

/**
 * Created by Alyx on 3/17/17.
 */

public class Clear {

    /**
     * Clears all tables in the database.
     * @throws DatabaseException
     */
    public void clearAllTables() throws DatabaseException {
        Database db = new Database();
        try {
            db.openTransaction();
            db.clearTables();
            db.closeTransaction(true);
        } catch (DatabaseException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}
