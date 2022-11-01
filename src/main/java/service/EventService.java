package service;

import dao.*;
import model.AuthToken;
import model.Event;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;

public class EventService {

    public EventResult event(String token, String eventID) {
        boolean success = false;
        String message;
        Database db = new Database();
        EventResult result = new EventResult();
        try {
            //open
            Connection connection = db.openConnection();
            EventDAO eDAO = new EventDAO(connection);
            AuthTokenDAO aDAO = new AuthTokenDAO(connection);
            AuthToken a = aDAO.find(token);
            if (a == null) {
                throw new DataAccessException("Authtoken invalid");
            }
            String userName = a.getUsername();
            Event event = eDAO.find(eventID);
            if (event == null) {
                throw new DataAccessException("Event with specified id not found in database");
            }
            if (!userName.equals(event.getAssociateUserName())) {
                throw new DataAccessException("Authtoken not associated with username");
            }
            //close
            db.closeConnection(true);
            String personID = event.getPersonId();
            float latitude = event.getLatitude();
            float longitude = event.getLongitude();
            String country = event.getCountry();
            String city = event.getCity();
            String eventType = event.getEventType();
            int year = event.getYear();
            success = true;
            result = new EventResult(userName, eventID, personID, latitude, longitude, country, city, eventType, year, success);
            return result;
        } catch(DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                System.out.println("No close event");
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            result = new EventResult(message, success);
            return result;
        }
    }
}
