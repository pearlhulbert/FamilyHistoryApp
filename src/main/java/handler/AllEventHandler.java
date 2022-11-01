package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.AllEventResult;
import result.FamilyResult;
import service.AllEventService;
import service.EventService;
import service.FamilySerivce;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class AllEventHandler extends SuperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers header = exchange.getRequestHeaders();
                if (header.containsKey("Authorization")) {
                    String token = header.getFirst("Authorization");

                    AllEventService eventServe = new AllEventService();
                    AllEventResult result = eventServe.allEvents(token);

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
