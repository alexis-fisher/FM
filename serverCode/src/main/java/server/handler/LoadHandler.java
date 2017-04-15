package server.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import server.dataAccess.Database;
import server.dataAccess.DatabaseException;
import server.model.Event;
import server.model.Person;
import server.model.User;
import server.services.Clear;
import server.services.Load;

/**
 * Created by Alyx on 3/4/17.
 */

public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        try {
            Converter c = new Converter();
            String requestBody = c.convertInputStreamToString(inputStream);
            //System.out.println(requestBody);


            Gson g = new Gson();
            //JsonObject root = g.fromJson(requestBody, JsonObject.class);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(requestBody);

            JsonArray usersJson = jsonObject.has("users") ? jsonObject.getAsJsonArray("users") : new JsonArray();
            JsonArray personsJson = jsonObject.has("persons") ? jsonObject.getAsJsonArray("persons") : new JsonArray();
            JsonArray eventsJson = jsonObject.has("events") ? jsonObject.getAsJsonArray("events") : new JsonArray();

            ArrayList<User> users = new ArrayList<>();
            for (JsonElement e : usersJson) {
                User user = g.fromJson(e, User.class);
                users.add(user);
            }

            ArrayList<Person> persons = new ArrayList<>();
            for (JsonElement e : personsJson) {
                Person person = g.fromJson(e, Person.class);
                persons.add(person);

            }

            ArrayList<Event> events = new ArrayList<>();
            for (JsonElement e : eventsJson) {
                Event event = g.fromJson(e, Event.class);
                events.add(event);
            }

            User[] userArray = new User[users.size()];
            int index = 0;
            for(User u : users){
                userArray[index] = u;
                index++;
            }
            index = 0;
            Person[] personArray = new Person[persons.size()];
            for(Person p : persons){
                personArray[index] = p;
                index++;
            }
            index = 0;
            Event[] eventArray = new Event[events.size()];
            for(Event e : events){
                eventArray[index] = e;
                index++;
            }

            String message = "";

            try {
                Clear clear = new Clear();
                clear.clearAllTables();
                System.out.println("Tables cleared!");
            }
            catch (DatabaseException e){
                System.out.println("Couldn't clear tables!");

            }
            Load load = new Load();

            try {
                message = load.loadAll(userArray, personArray, eventArray);
            } catch (DatabaseException e){
                message = e.getMessage();
            }

            JsonObject response = c.responseMessage(message);

            c.sendResponse(response, httpExchange);

        } catch (IOException e) {
            System.out.println("Load handler error!");
            e.printStackTrace();
        }
    }


}