package server;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handler.*;

public class Server {
    private HttpServer server;
    private static final int MAX_WAITING_CONNECTIONS = 12;

    public Server() {
    }

    private void run(String portNumber) {
        System.out.println("Intit Http Server");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.createContext("/", new FileHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new FamilyHandler());
        server.createContext("/event", new AllEventHandler());
        server.createContext("/event/", new EventHandler());
        server.start();
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
