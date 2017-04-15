package server.proxy;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import server.handler.Converter;
import server.model.Event;
import server.model.Person;
import server.request.FillRequest;
import server.request.LoadRequest;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.ClearResult;
import server.result.EventResult;
import server.result.FillResult;
import server.result.LoadResult;
import server.result.LoginResult;
import server.result.PersonResult;
import server.result.RegisterResult;



/**
 * Created by Alyx on 2/17/17.
 */


public class ServerProxy {

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken;

    private static String host;
    private static int port;
    private static String url;

    public static ServerProxy server(){
        if(_instance == null){
            _instance = new ServerProxy();
        }
        return _instance;
    }

    private static ServerProxy _instance;

    private ServerProxy() {
        host = "192.168.1.253";
        port = 8080;
        url = "http://" + host + ":" + port;
    }

    private ServerProxy(String host, String port) {
        this.host = host;
        this.port = Integer.parseInt(port);
        this.url = "http://" + this.host + ":" + this.port;
    }

    private ServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.url = "http://" + this.host + ":" + this.port;

    }

    public String post(String urlPath, Object request) throws ClientException {
        try {
            URL fullURL = new URL(this.url + urlPath);
            HttpURLConnection conn = (HttpURLConnection) fullURL.openConnection();

            // Allow for input & output
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Set to POST, since this is the post method...
            conn.setRequestMethod("POST");

            // Add so JSON can pass thru
            conn.addRequestProperty("Accept", "application/json");

            // Connect!
            conn.connect();

            // Send through JSON
            Gson g = new Gson();
            String json = g.toJson(request);

            OutputStreamWriter requestBody = new OutputStreamWriter(conn.getOutputStream());
            requestBody.write(json);
            requestBody.close();


            Converter c = new Converter();
            // Get response
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream output = conn.getInputStream();

                // Convert to JSON for easy casting
                String databaseResponse = c.convertInputStreamToString(output);

                output.close();

                // Convert output from Database into useful info
                // (is String OK? then convert that to Object from JSON?)
                return databaseResponse;
            } else {
                // failed.
                return "Did not work!";
            }

        } catch (MalformedURLException e) {
            throw new ClientException(e.getMessage());
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
    }

    public String get(String urlPath) throws ClientException {
        try {
            Converter c = new Converter();

            // Open connection
            URL fullURL = new URL(this.url + urlPath);
            HttpURLConnection conn = (HttpURLConnection) fullURL.openConnection();

            // Enable output reception
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            // Add auth token
            conn.addRequestProperty("Authorization", authToken);


            // Add so JSON can pass thru
            conn.addRequestProperty("Accept", "application/json");

            // Connect!
            conn.connect();

            // Get response
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream output = conn.getInputStream();

                // Convert to JSON for easy converting to a class
                String databaseResponse = c.convertInputStreamToString(output);

                output.close();

                // Convert output from Database into useful info
                // (is String OK? then convert that to Object from JSON?)
                return databaseResponse;
            }
            else{
                return "HTTP code not OK!";
            }

        } catch (MalformedURLException e) {
            throw new ClientException(e.getMessage());
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     *
     * @param r RegisterRequest (see corresponding API)
     * @return RegisterResult (see corresponding API)
     */
    public RegisterResult register(RegisterRequest r) throws ClientException {
        Gson gson = new Gson();
        RegisterResult result = null;
        result = gson.fromJson(post("/user/register", r), RegisterResult.class);
        authToken = result.getAuthToken();
        if (result == null) {
            throw new ClientException("Response code NOT httpOK");
        }
        return result;
    }

    /**
     * Logs the user in, and returns an authToken
     *
     * @param r LoginRequest (see corresponding API)
     * @return LoginResult (see corresponding API)
     */
    public LoginResult login(LoginRequest r) throws ClientException {
        Gson gson = new Gson();
        LoginResult result = gson.fromJson(post("/user/login", r), LoginResult.class);
        authToken = result.getAuthToken();
        if (result == null) {
            throw new ClientException("Response code NOT httpOK");
        }
        return result;
    }

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     *
     * @return ClearResult - success or failure message.
     */
    public ClearResult clear() {
        Gson gson = new Gson();
        ClearResult result = null;
        try {
            result = gson.fromJson(get("/clear/"), ClearResult.class);
            if (result == null) {
                throw new ClientException("Response code NOT httpOK");
            }
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     *
     * @return FillResult (see corresponding API)
     */
    public FillResult fill(FillRequest f) {
        Gson gson = new Gson();
        if (f.getGenerations() != 0) {
            FillResult result = null;
            try {
                result = gson.fromJson(get("/fill/" + f.getUserName() + "/" + f.getGenerations()), FillResult.class);
                if (result == null) {
                    throw new ClientException("Response code NOT httpOK");
                }
            } catch (ClientException e) {
                System.out.println(e.getMessage());
            }
            return result;
        } else {
            FillResult result = null;
            try {
                result = gson.fromJson(get("/fill/" + f.getUserName()), FillResult.class);
                if (result == null) {
                    throw new ClientException("Response code NOT httpOK");
                }
            } catch (ClientException e) {
                System.out.println(e.getMessage());
            }
            return result;
        }
    }

    /**
     * Clears all data from the database, and then loads the posted user, person, and event data into the database.
     *
     * @param r LoadRequest (see corresponding API)
     * @return LoadResult (see corresponding API)
     */
    public LoadResult load(LoadRequest r) {
        Gson gson = new Gson();
        LoadResult result = null;
        try {
            result = gson.fromJson(post("/load/", r), LoadResult.class);
            if (result == null) {
                throw new ClientException("Response code NOT httpOK");
            }
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Returns the single Person object with the specified ID.
     *
     * @return PersonResult (see corresponding API)
     */
    public PersonResult person(String personID) {
        Gson gson = new Gson();
        PersonResult personResult = new PersonResult();
        try {
            Person person = gson.fromJson(get("/person/" + personID), Person.class);
            personResult.setPerson(person);
            if (person == null) {
                throw new ClientException("Response code NOT httpOK");
            }

        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        return personResult;
    }

    /**
     * Returns ALL family members of the current user.
     *
     * @return Array of Person objects
     */
    public PersonResult person() throws ClientException {
        Gson gson = new Gson();
        PersonResult result = gson.fromJson(get("/person/"), PersonResult.class);
        if (result == null) {
            throw new ClientException("Response code NOT httpOK");
        }
        return result;
    }

    /**
     * Returns the single Event object with the specified ID
     *
     * @return EventResult (see corresponding API)
     */
    public EventResult event(String eventID) {
        Gson gson = new Gson();
        EventResult result = new EventResult();
        try {
            Event event = gson.fromJson(get("/event/" + eventID), Event.class);
            if(event.getEventID().equals("")){
                result = gson.fromJson(get("/event/" + eventID), EventResult.class);
            } else {
                result.setEvent(event);
            }
            if (result == null) {
                throw new ClientException("Response code NOT httpOK");
            }
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Returns ALL events for ALL family members of the current user
     *
     * @return An array of all the events for a current user
     */
    public EventResult event() throws ClientException {
        Gson gson = new Gson();
        EventResult result = gson.fromJson(get("/event/"), EventResult.class);
        if (result == null) {
            throw new ClientException("Response code NOT http OK");
        }
        return result;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ServerProxy.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ServerProxy.port = port;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ServerProxy.url = url;
    }


    public static void main(String[] args) {

    }
}
