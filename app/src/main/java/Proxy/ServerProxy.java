package Proxy;

import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import com.google.gson.Gson;

import Data.DataCache;
import model.Person;
import request.AllEventRequest;
import request.FamilyRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.ClearResult;
import result.FamilyResult;
import result.LoginResult;
import result.RegisterResult;

public class ServerProxy {

    private String serverHost;
    private String serverPort;
    private DataCache instance;

   public ServerProxy(String host, String port) {
       this.serverHost = host;
       this.serverPort = port;
   }

    public LoginResult login(LoginRequest request) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            String data = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(data, reqBody);
            reqBody.close();
            LoginResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Login success!");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                respBody.close();
                result = gson.fromJson(respData, LoginResult.class);
                String currId = result.getPersonID();
                instance = DataCache.getInstance();
                instance.setUserPersonId(currId);
                instance.setCurrAuthtoken(result.getAuthtoken());
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                respBody.close();

                System.out.println(respData);

                result = gson.fromJson(respData, LoginResult.class);
                return result;
            }
        }
        catch (MalformedURLException m) {
            m.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult register(RegisterRequest request) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            String data = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(data, reqBody);
            reqBody.close();
            RegisterResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Register success!");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                instance = DataCache.getInstance();
                result = gson.fromJson(respData, RegisterResult.class);
                respBody.close();
                Person newPerson = new Person(result.getPersonID(), request.getUsername(), request.getFirstName(),
                        request.getLastName(), request.getGender());
                instance.setCurrPerson(newPerson);
                instance.setCurrAuthtoken(result.getAuthtoken());
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);


                System.out.println(respData);

                result = gson.fromJson(respData, RegisterResult.class);
                respBody.close();
                return result;

            }
        }
        catch (MalformedURLException m) {
            m.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FamilyResult getFamily() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            instance = DataCache.getInstance();
            String token = instance.getCurrAuthtoken();
            http.addRequestProperty("Authorization", token);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                respBody.close();
                FamilyResult result = gson.fromJson(respData, FamilyResult.class);
                String id = instance.getUserPersonId();
                Person[] currFamily = result.getFamily();
                for (int i = 0; i < currFamily.length; ++i) {
                    if (currFamily[i].getPersonID().equals(id)) {
                        instance.setCurrPerson(currFamily[i]);
                        break;
                    }
                }
                System.out.println("Family success!");
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                respBody.close();

                System.out.println(respData);
            }
        }
        catch (MalformedURLException m) {
            m.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AllEventResult getEvents() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            instance = DataCache.getInstance();
            String token = instance.getCurrAuthtoken();
            http.addRequestProperty("Authorization", token);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            Gson gson = new Gson();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Event success!");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                respBody.close();
                AllEventResult result = gson.fromJson(respData, AllEventResult.class);
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                respBody.close();

                System.out.println(respData);
            }
        }
        catch (MalformedURLException m) {
            m.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClearResult clear() {
       try {
           URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");
           HttpURLConnection http = (HttpURLConnection) url.openConnection();
           http.setRequestMethod("POST");
           http.setDoOutput(false);
           http.connect();
           Gson gson = new Gson();
           if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
               System.out.println("Clear success!");
               InputStream respBody = http.getInputStream();
               String respData = readString(respBody);
               respBody.close();
               ClearResult result = gson.fromJson(respData, ClearResult.class);
               return result;
           }
           else {
               System.out.println("ERROR: " + http.getResponseMessage());

               InputStream respBody = http.getErrorStream();

               String respData = readString(respBody);

               respBody.close();

               System.out.println(respData);
           }
       }
       catch (MalformedURLException m) {
           m.printStackTrace();
       }
       catch (IOException e) {
           e.printStackTrace();
       }
        return null;
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
