package server;

/**
 * Created by Alyx on 3/20/17.
 */

public class TestDriver {
    // Set port & host

    // run tests
    public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(
            "server.dataAccess.AuthTokenDaoTest",
            "server.dataAccess.EventDaoTest",
            "server.dataAccess.PersonDaoTest",
            "server.dataAccess.UserDaoTest",
            "com.example.alyx.proxy.ServerProxyTest");
    }
}
