package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import dao.DataAccessException;
import model.LocationArray;
import request.FillRequest;
import result.FillResult;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Vector;

public class FillHandler extends SuperHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                String currUri = exchange.getRequestURI().toString();
                String userName = "";
                int generationNum = 0;
                int numSlashes = 0;
                Vector<Integer> indexes = new Vector<>();
                for (int i = 0; i < currUri.length(); ++i) {
                    if (currUri.charAt(i) == '/') {
                        if (i != 0) {
                            indexes.add(i);
                        }
                        ++numSlashes;
                    }
                }
                if (numSlashes == 2) {
                    userName = currUri.substring(currUri.lastIndexOf("/")+1);
                    generationNum = 4;
                }
                else if (numSlashes == 3) {
                    userName = currUri.substring((indexes.elementAt(0) + 1), (currUri.lastIndexOf("/")));
                    generationNum = Integer.parseInt(currUri.substring(indexes.elementAt(1)+1));
                }
                Gson gson = new Gson();
                FillService fServe = new FillService();

                FillRequest fillRequest = new FillRequest(userName, generationNum);
                FillResult result = fServe.fill(fillRequest);

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
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException except) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            exchange.getResponseBody().close();
            except.printStackTrace();
        }
    }

}
