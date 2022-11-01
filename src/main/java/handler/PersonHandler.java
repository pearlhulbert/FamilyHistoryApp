package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import dao.AuthTokenDAO;
import dao.Database;
import model.AuthToken;
import model.Person;
import request.PersonRequest;
import result.PersonResult;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class PersonHandler extends SuperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers header = exchange.getRequestHeaders();
                if (header.containsKey("Authorization")) {
                    String token = header.getFirst("Authorization");
                    String ID = exchange.getRequestURI().toString();
                    String personID = ID.substring(ID.lastIndexOf("/")+1);

                    PersonService pServe = new PersonService();
                    PersonResult result = pServe.person(token, personID);

                    Gson gson = new Gson();
                    if (result.isSuccess()) {
                        success = true;
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }

                    OutputStream responseBody = exchange.getResponseBody();
                    String toWrite = gson.toJson(result);
                    writeString(toWrite, responseBody);
                    exchange.getResponseBody().close();
                }
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
