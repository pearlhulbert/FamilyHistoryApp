package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DataAccessException;
import result.ClearResult;
import service.ClearService;
import java.io.OutputStream;


import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler extends SuperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                ClearService cServive = new ClearService();

                Gson gson = new Gson();

               ClearResult result = cServive.clear();

                if (result.isSuccess()) {
                    success = true;
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream body = exchange.getResponseBody();
                writeString(gson.toJson(result), body);
                exchange.getResponseBody().close();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException except) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            exchange.getResponseBody().close();
            except.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

}
