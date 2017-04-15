package server.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.org.apache.bcel.internal.generic.ObjectType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Alyx on 3/15/17.
 */

public class Converter {
    public JsonObject responseMessage(String message){
        JsonObject json = new JsonObject();
        json.addProperty("message",message);
        return json;
    }


    public String convertInputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public void sendResponse(Object r, HttpExchange e){
        if(r == null){
            // fail!
            System.out.println("Send response fail!!!!!");
        }
        else{
            try {
                e.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStreamWriter response = new OutputStreamWriter(e.getResponseBody());
                Gson toGson = new GsonBuilder().disableHtmlEscaping().create();
                response.write(toGson.toJson(r));
                response.close();

            } catch (IOException e1) {
                System.out.println("Couldn't send response!");
                e1.printStackTrace();
            }
        }
    }

}
