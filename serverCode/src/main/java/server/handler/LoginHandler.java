package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import server.dataAccess.DatabaseException;
import server.services.Login;
import server.services.Register;

/**
 * Created by Alyx on 3/4/17.
 */

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        InputStream requestBody = httpExchange.getRequestBody();
        Converter conn = new Converter();
        Gson gson = new Gson();
        String input = conn.convertInputStreamToString(requestBody);
        JsonObject json = gson.fromJson(input,JsonObject.class);
        if(json != null) {
            if (json.has("userName") && json.has("password")) {
                Login l = new Login();
                try {
                    l.login(json.get("userName").getAsString(),json.get("password").getAsString());

                    // if the login worked, send back the token!
                    if (!l.getUserName().equals("")) {
                        //send response!
                        conn.sendResponse(l, httpExchange);
                    } else {
                        // send fail message
                        JsonObject j = conn.responseMessage("Login failed");
                        conn.sendResponse(j, httpExchange);
                    }

                } catch (DatabaseException e){
                    JsonObject j = conn.responseMessage(e.getMessage());
                    conn.sendResponse(j, httpExchange);
                }
            }
            else{
                JsonObject j = conn.responseMessage("Missing username or password. Check the JSON!");
                conn.sendResponse(j, httpExchange);
            }
        }
        else{
            JsonObject j = conn.responseMessage("JSON is null. Why?");
            conn.sendResponse(j, httpExchange);
        }

    }
}
