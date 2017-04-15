package server.server;

/**
 * Created by Alyx on 3/4/17.
 */

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import server.handler.ClearHandler;
import server.handler.DefaultHandler;
import server.handler.EventHandler;
import server.handler.FillHandler;
import server.handler.LoadHandler;
import server.handler.LoginHandler;
import server.handler.PersonHandler;
import server.handler.RegisterHandler;


public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null); // use the default executor

        System.out.println("Creating contexts");
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/", new DefaultHandler());


        System.out.println("Starting server");
        server.start();
    }

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Invalid. You must enter a port number as an argument");
        }
        else {
            String portNumber = args[0];
            new Server().run(portNumber);
        }
    }
}
