package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.LoadResult;
import service.LoadSerivce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends SuperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                LoadSerivce lServe = new LoadSerivce();

                Gson gson = new Gson();
                InputStream body = exchange.getRequestBody();
                String data = readString(body);

                LoadRequest loadRequest = gson.fromJson(data, LoadRequest.class);

                LoadResult loadResult = lServe.load(loadRequest);

                if (loadResult.isSuccess()) {
                    success = true;
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream responseBody = exchange.getResponseBody();
                writeString(gson.toJson(loadResult), responseBody);
                exchange.getResponseBody().close();
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
