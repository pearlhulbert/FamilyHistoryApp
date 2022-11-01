package service;

import com.sun.net.httpserver.Headers;
import dao.*;
import model.Person;
import request.FillRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.FillResult;
import result.LoginResult;
import result.RegisterResult;
import model.User;
import model.Event;
import java.util.Vector;

import java.sql.Connection;
import java.util.TreeSet;
import java.util.UUID;

public class RegisterService {

    /** this method registers a user by creating a user, as well as a person, tree and an authtoken for that user
     * @param request is the info needed to create model objects for use in dao classes
     * @return the registered user and their info, as well as whether the request succeeded or fail
     */
    public RegisterResult registerUser(RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String gender = request.getGender();
        String personID = UUID.randomUUID().toString();
        User newUser = new User(username, password, email, firstName, lastName, gender, personID);
        Database db = new Database();
        boolean success = false;
        String message = "";

        try {
            Connection conn = db.openConnection();
            UserDAO uDAO = new UserDAO(conn);

            uDAO.insert(newUser);
            db.closeConnection(true);

        } catch (DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            RegisterResult result = new RegisterResult(message, success);
            return result;
        }

        FamilyTree familyTree = new FamilyTree();
        Vector<Event> events = new Vector<>();
        Vector<Person> people = new Vector<>();
        familyTree.generateRoot(username, 4, events, people, newUser);

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);

            for (Event e : events) {
                if (e.getYear() == 1700) {
                    e.setPersonId(newUser.getPersonID());
                }
                eventDAO.insert(e);
            }

            for (Person p : people) {
                if (!p.hasSpouse()) {
                    p.setFirstName(newUser.getFirstName());
                    p.setLastName(newUser.getLastName());
                    p.setPersonID(newUser.getPersonID());
                }
                personDAO.insert(p);
            }

            db.closeConnection(true);
        } catch (DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("No close register");
                e.printStackTrace();
            }
            RegisterResult result = new RegisterResult(message, success);
            return result;
        }

        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginService loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest);

        String authtoken = loginResult.getAuthtoken();

        success = true;
        RegisterResult result = new RegisterResult(authtoken, username, personID, success);
        return result;
    }

}