package server.dataAccess;

/**
 * Created by Alyx on 3/11/17.
 */

public class DatabaseException extends Exception{

        public DatabaseException(String s) {
            super(s);
        }

        public DatabaseException(String s, Throwable throwable) {
            super(s, throwable);
        }
    
}
