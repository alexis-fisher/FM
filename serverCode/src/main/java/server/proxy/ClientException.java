package server.proxy;

/**
 * Created by Alyx on 3/18/17.
 */

public class ClientException extends Throwable {
    public ClientException(String s) {
        super(s);
    }

    public ClientException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
