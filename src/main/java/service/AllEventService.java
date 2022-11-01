package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import request.AllEventRequest;
import result.AllEventResult;
import result.FamilyResult;

import java.sql.Connection;
import java.util.List;

public class AllEventService {

    /** method returns all the events for the associated user according to authtoken
     * @param token request containing user's authtoken
     * @return an Event array of all events stored in result
     */
    public AllEventResult allEvents(String token) {
        boolean success = false;
        String message;
        AllEventResult result = new AllEventResult();
        Database db = new Database();
        try {
            //open
            Connection connection = db.openConnection();

            AuthTokenDAO aDAO = new AuthTokenDAO(connection);
            AuthToken aToken = aDAO.find(token);
            if (aToken == null) {
                throw new DataAccessException("Invalid authtoken/authtoken not found");
            }

            String userName = aToken.getUsername();
            EventDAO eDAO = new EventDAO(connection);
            List<Event> events = eDAO.getUserEvents(userName);
            Event[] eventArray = new Event[events.size()];
            eventArray = events.toArray(eventArray);
            //close
            db.closeConnection(true);
            success = true;
            result = new AllEventResult(eventArray, success);
            return result;
        } catch(DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                //close
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("no close allevent");
                e.printStackTrace();
            }
            result = new AllEventResult(message, success);
            return result;
        }
    }
}
