package server.handler;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import server.dataAccess.DatabaseException;


/**
 * Created by Alyx on 3/4/17.
 */

public class DefaultHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Headers respHeaders = httpExchange.getResponseHeaders();
        try {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } catch (IOException e) {
            System.out.println("Couldn't send response headers!");
        }
        Scanner s = null;
        OutputStreamWriter sw = new OutputStreamWriter(httpExchange.getResponseBody());

        String url = httpExchange.getRequestURI().getPath();
        String[] components = url.split("/");

        try{
            if(components.length <= 1 || components[1].equals("")) {
                s = new Scanner(new FileReader("data/HTML/index.html"));
            }
        }
        catch (IOException e){
            try {
                s = new Scanner(new FileReader("data/HTML/404.html"));
            }
            catch (IOException m){
                System.out.println("Can't find any of the HTML files. Fix the path!");
                m.printStackTrace();
            }
        }

        StringBuilder webpage = new StringBuilder();
        while(s.hasNextLine()){
            webpage.append(s.nextLine() + "\n");
        }

        s.close();
        sw.write(webpage.toString());
        sw.close();


    }

}
