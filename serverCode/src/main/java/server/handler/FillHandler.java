package server.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.Person;
import server.services.Fill;

/**
 * Created by Alyx on 3/4/17.
 */

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI urlInfo = httpExchange.getRequestURI();
        String url = urlInfo.toString();

        String[] urlComponents = url.split("/");

        Converter conn = new Converter();

        if (urlComponents.length == 3) {  // generate 4 generations
            try {
                Fill fill = new Fill();

                // Get username
                String userName = urlComponents[2];

                // Clear existing generations for user
                fill.dropUsersEvents(userName);
                fill.dropUsersPersons(userName);

                // fill 4 generations (automatic)
                fill.fillFourGenerations(userName);

                // Send response
                JsonObject j = conn.responseMessage("Successfully added " + fill.getPeopleCreated() + " persons and " +
                                                    fill.getEventsCreated() + " events to the database.");
                conn.sendResponse(j, httpExchange);

            } catch (DatabaseException e){
                // Send response
                JsonObject j = conn.responseMessage(e.getMessage());
                conn.sendResponse(j, httpExchange);
            }
        }
        else if (urlComponents.length > 3) {  // generate 4 generations
            try {
                Fill fill = new Fill();

                // Get userName from URL
                String userName = urlComponents[2];

                // Clear existing generations for user
                fill.dropUsersEvents(userName);
                fill.dropUsersPersons(userName);

                // specify generations
                String generations = urlComponents[3];
                int gens = Integer.parseInt(generations);

                if (gens <= 0) { // invalid #
                    // Send response
                    JsonObject j = conn.responseMessage("0 or more generations input, so I'm not going to add any more generations!");
                    conn.sendResponse(j, httpExchange);
                } else {
                    fill.fillThisManyGenerations(userName, gens);
                    // Send response
                    JsonObject j = conn.responseMessage("Successfully added " + fill.getPeopleCreated() + " persons and " +
                            fill.getEventsCreated() + " events to the database.");
                    conn.sendResponse(j, httpExchange);
                }
            }
            catch (DatabaseException e){
                // Send fail response
                JsonObject j = conn.responseMessage(e.getMessage());
                conn.sendResponse(j, httpExchange);
                return;
            }
        }
        else{
            // Send response for fail with description of the error
            JsonObject j = conn.responseMessage("URL components are not correct. Format: /fill/[userName]/{generations}");
            conn.sendResponse(j,httpExchange);
        }
    }

}
