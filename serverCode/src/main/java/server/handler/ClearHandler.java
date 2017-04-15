package server.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.User;
import server.services.Clear;

/**
 * Created by Alyx on 3/4/17.
 */

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Converter con = new Converter();
        try{
            Clear clearAll = new Clear();
            clearAll.clearAllTables();

            JsonObject response = con.responseMessage("Clear succeeded.");
            con.sendResponse(response, httpExchange);
        }
        catch (DatabaseException e){
            JsonObject response = con.responseMessage(e.getMessage());
            con.sendResponse(response, httpExchange);
            System.out.println("Couldn't clear database!");
        }
    }
}
