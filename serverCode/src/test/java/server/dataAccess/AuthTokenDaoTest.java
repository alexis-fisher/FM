package server.dataAccess;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.model.AuthToken;

import static org.junit.Assert.assertTrue;


/**
 * Created by Alyx on 3/18/17.
 */
public class AuthTokenDaoTest {
    Database db = new Database();
    AuthTokenDao ad = new AuthTokenDao(db);

    @Before
    public void setUp() throws Exception {
        // Open transaction
        db.openTransaction();

        // Ensure token table is empty
        ad.deleteAllTokens();

    }

    @After
    public void tearDown() throws Exception {
        // Close transaction
        db.closeTransaction(true);

    }

    @Test
    public void testCreateToken() throws Exception {
        // Create the token
        ad.createToken("token_placeholder", "userName_placeholder", "personID_placeholder");

        // Make sure the token is in the DB
        AuthToken[] tokens = ad.read();
        for (AuthToken p : tokens) {
            assertTrue(p.getToken().equals("token_placeholder"));
        }
    }

    @Test
    public void testRead() throws Exception {
        // Create token
        ad.createToken("token_placeholder", "userName_placeholder", "personID_placeholder");
        ad.createToken("token_placeholder1", "userName_placeholder", "personID_placeholder");

        // Read tokens from DB and assert that they are what was created (they were read correctly)
        AuthToken[] tokens = ad.read();
        assertTrue (tokens != null);

        // Since we don't know what order the DB will read them out in, we just have to ensure that both were entered
        // The DB will not take duplicates, so this is a valid way to test that both were entered & read.
        assertTrue(tokens[0].getToken().equals("token_placeholder") || tokens[0].getToken().equals("token_placeholder1") );
        assertTrue(tokens[1].getToken().equals("token_placeholder1") || tokens[1].getToken().equals("token_placeholder") );


    }

    @Test
    public void testDeleteToken() throws Exception {
        // put someone in the tokens table
        ad.createToken("token_placeholder", "userName_placeholder", "personID_placeholder");

        // read data out of tokens table - there should be something there
        AuthToken[] tokens = ad.read();

        // ensure that create added something
        assertTrue(tokens.length != 0);

        // delete it from the table
        ad.deleteToken("token_placeholder");

        // read data out of the tokens table - there should be nothing there now
        AuthToken[] tokens1 = ad.read();

        // if anything in tokens table, throw exception
        assertTrue(tokens1.length == 0 || tokens1 == null);
    }

    @Test
    public void testDeleteAllTokens() throws Exception {
        // put stuff in the tokens table
        ad.createToken("token_placeholder", "userName_placeholder", "personID_placeholder");
        ad.createToken("token_placeholder1", "userName_placeholder", "personID_placeholder");


        // read data out of tokens table - there should be something there
        AuthToken[] tokens = ad.read();

        // ensure that create added something
        assertTrue(tokens.length != 0);

        // clear the tokens table
        ad.deleteAllTokens();

        // read data out of the tokens table - there should be nothing there now
        AuthToken[] tokens1 = ad.read();

        // if anything in tokens table, throw exception
        assertTrue(tokens1.length == 0 || tokens1 == null);
    }


}