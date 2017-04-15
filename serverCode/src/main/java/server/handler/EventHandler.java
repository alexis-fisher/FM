package server.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.services.Authorization;
import server.services.Event;

/**
 * Created by Alyx on 3/4/17.
 */

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI url = httpExchange.getRequestURI();
        String translateToFilePath = url.toString();

        String[] urlComponents = translateToFilePath.split("/");

        String authToken = httpExchange.getRequestHeaders().getFirst("Authorization");
        Converter conn = new Converter();
        if (authToken != null) {
            Authorization authorize = new Authorization();
            try {
                if (authorize.authenticate(authToken)){
                    if (urlComponents.length < 3) {
                        // get all events

                        String userName = null;

                        try {
                            // get user (from token)
                            userName = authorize.getTokenOwner(authToken);
                        } catch (DatabaseException e){
                            JsonObject j = conn.responseMessage(e.getMessage());
                            conn.sendResponse(j, httpExchange);
                            return;
                        }

                        if(userName != null) {
                            // get events with the descendant listed as the userName
                            server.services.Event eventGetter = new Event();
                            server.model.Event[] ev = eventGetter.getEventByUserName(userName);

                            // if the events exist, send them back
                            if (ev != null && ev.length > 0) {
                                // package and send

                                // First, turn into object, not array of objects
                                JsonObject jsonObject = new JsonObject();
                                Gson g = new Gson();
                                jsonObject.add("data", g.toJsonTree(ev));


                                //send response!
                                conn.sendResponse(jsonObject, httpExchange);
                            } else {
                                // send fail message
                                JsonObject j = conn.responseMessage("No events found");
                                conn.sendResponse(j, httpExchange);
                            }
                        } else {
                            // send fail message
                            JsonObject j = conn.responseMessage("No user associated with that authToken");
                            conn.sendResponse(j, httpExchange);
                        }
                    } else if (urlComponents.length == 3) {  // get event by id

                        // get name of event from path
                        String eventID = urlComponents[2];

                        // if the event exists, send it back
                        Event eventGetter = new Event();
                        server.model.Event ev = new server.model.Event();
                        try {
                            ev = eventGetter.getEventByID(eventID);
                        } catch (DatabaseException e){
                            JsonObject j = conn.responseMessage(e.getMessage());
                            conn.sendResponse(j,httpExchange);
                        }
                        if(!ev.getEventID().equals("")){
                            // package & send
                            conn.sendResponse(ev,httpExchange);
                        }
                        else{
                            // send error message
                            JsonObject j = conn.responseMessage("No events with that ID found");
                            conn.sendResponse(j,httpExchange);
                        }
                    } else {
                        // send error message
                        JsonObject j = conn.responseMessage("Invalid number of arguments");
                        conn.sendResponse(j,httpExchange);
                    }
                } else{
                    // send message AUTH TOKEN failed!
                    JsonObject j = conn.responseMessage("Authorization token not recognized");
                    conn.sendResponse(j,httpExchange);
                }
            } catch (DatabaseException e) {
                // get message from failed function, and send it!
                JsonObject j = conn.responseMessage(e.getMessage());
                conn.sendResponse(j,httpExchange);
            }
        } else {
            // send message auth token not found!
            JsonObject j = conn.responseMessage("Authorization token not sent");
            conn.sendResponse(j,httpExchange);
        }
    }
}
