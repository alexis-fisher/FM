package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import server.dataAccess.DatabaseException;
import server.model.Person;
import server.services.Authorization;

/**
 * Created by Alyx on 3/4/17.
 */

public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI url = httpExchange.getRequestURI();
        String translateToFilePath = url.toString();

        String[] urlComponents = translateToFilePath.split("/");

        // Do I check for an auth token here - YES

        String authToken = httpExchange.getRequestHeaders().getFirst("Authorization");
        Converter conn = new Converter();

        if(authToken != null) {
            Authorization auth = new Authorization();
            try {
                if (auth.authenticate(authToken)) {
                    if (urlComponents.length < 3) {
                        // get all persons

                        // get user (from token)
                        String username = null;
                        username = auth.getTokenOwner(authToken);


                        if(username != null) {
                            // get names of persons user is associated with
                            server.services.Person personGetter = new server.services.Person();
                            server.model.Person[] persons = personGetter.getPersonsByUserName(username);

                            // if the events exist, send them back
                            if (persons != null && persons.length > 0) {
                                // package and send

                                // First, turn into object, not array of objects
                                JsonObject jsonObject = new JsonObject();
                                Gson g = new Gson();
                                jsonObject.add("data", g.toJsonTree(persons));


                                //send response!
                                conn.sendResponse(jsonObject, httpExchange);
                            } else {
                                // send fail message
                                JsonObject j = conn.responseMessage("No persons found");
                                conn.sendResponse(j, httpExchange);
                            }
                        }
                        else{
                            System.out.println("Username is null! Auth token belongs to no user!");
                        }
                        // if the names exist, send them back
                        // if not, send error
                    } else if (urlComponents.length == 3) {

                        // get user (from token)
                        String username = null;
                        username = auth.getTokenOwner(authToken);

                        // get person by id
                        String personID = urlComponents[2];

                        // get name of person from id
                        server.services.Person personGetter = new server.services.Person();
                        Person pers = personGetter.getPersonFromID(personID);
                        if(pers.getDescendant().equals(username)){
                            if (!pers.getPersonID().equals("")) {
                                // package & send
                                conn.sendResponse(pers, httpExchange);
                            } else {
                                // send error message
                                JsonObject j = conn.responseMessage("No persons with that ID found");
                                conn.sendResponse(j, httpExchange);
                            }
                        }
                        else{
                            // send error message
                            JsonObject j = conn.responseMessage("You're not the descendant of that user!");
                            conn.sendResponse(j, httpExchange);
                        }
                    }
                    else{
                        // send error message
                        JsonObject j = conn.responseMessage("Invalid number of arguments");
                        conn.sendResponse(j,httpExchange);
                    }
                }
                else{
                    // send message AUTH TOKEN failed!
                    JsonObject j = conn.responseMessage("Authorization token not recognized");
                    conn.sendResponse(j,httpExchange);
                }
            } catch (DatabaseException e) {
                JsonObject j = conn.responseMessage(e.getMessage());
                conn.sendResponse(j,httpExchange);
            }
        } else {
            // send message auth token not found!
        }



    }
}
