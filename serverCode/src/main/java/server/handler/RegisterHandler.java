package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by Alyx on 3/4/17.
 */
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import server.dataAccess.DatabaseException;
import server.services.Authorization;
import server.services.Fill;
import server.services.Login;
import server.services.Register;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        Converter conn = new Converter();

        // Get the data
        InputStream reqBody = httpExchange.getRequestBody();
        try {
            String jsonString = conn.convertInputStreamToString(reqBody);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(jsonString, JsonObject.class);

            // Add user to the User table in database
            Register register = new Register();
            if (json.has("userName") && json.has("password")
                    && json.has("email") && json.has("firstName")
                    && json.has("lastName") && json.has("gender")) {

                try {
                    register.addUser(json.get("userName").getAsString(),
                            json.get("password").getAsString(),
                            json.get("email").getAsString(),
                            json.get("firstName").getAsString(),
                            json.get("lastName").getAsString(),
                            json.get("gender").getAsString()
                    );

                    Fill fill = new Fill();
                    fill.fillFourGenerations(json.get("userName").getAsString());
                } catch (DatabaseException e) {
                    JsonObject j = conn.responseMessage(e.getMessage());
                    conn.sendResponse(j, httpExchange);
                    return;
                }

                // Once user has been added, log them in!
                Login l = new Login();
                try {
                    l.login(register.u.getUserName(), register.u.getPassword());
                } catch (DatabaseException e) {
                    JsonObject j = conn.responseMessage(e.getMessage());
                    conn.sendResponse(j, httpExchange);
                    return;
                }

                // if the login worked, send back the token!
                if (l != null && !l.getUserName().equals("")) {
                    //send response!
                    conn.sendResponse(l, httpExchange);
                } else {
                    // send fail message
                    JsonObject j = conn.responseMessage("Login failed");
                    conn.sendResponse(j, httpExchange);
                }
            } else {
                JsonObject j = conn.responseMessage("Fields are missing, cannot register");
                conn.sendResponse(j, httpExchange);
            }
        } catch (IOException e){
            JsonObject j = conn.responseMessage("Couldn't get input stream");
            conn.sendResponse(j, httpExchange);
        }
    }
}



