package dao;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;


public class EventDAO {
    private final Connection accessCon;

    public EventDAO(Connection con) {this.accessCon = con;}

    /** method used to insert an event object into the database table using sql commands as string
     * @param event is the event object to be inserted
     */
    public void insert(Event event) throws DataAccessException {
        String sqlString = "INSERT INTO event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = accessCon.prepareStatement(sqlString)) {
                //Using the statements built-in set(type) functions we can pick the question mark we want
                //to fill in and give it a proper value. The first argument corresponds to the first
                //question mark found in our sql String
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getAssociateUserName());
                stmt.setString(3, event.getPersonId());
                stmt.setFloat(4, event.getLatitude());
                stmt.setFloat(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setInt(9, event.getYear());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while inserting into the database");
            }


    }

    /** method used to find an event in the database based on event ID.
     * These commands will also be in SQL
     * @param eventID is a stirng representing eventID
     * @return the event matching this eventID
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE eventID = ?;";
        try (PreparedStatement stmt = accessCon.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /** this method removes an event from the database table
     * @param username event to be removed
     */
    public void remove(String username) throws DataAccessException {
        String sqlString = "DELETE FROM event WHERE associatedUsername = \'" + username + "\'";

        try (PreparedStatement stmt = accessCon.prepareStatement(sqlString)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while removing from the database");
        }
    }

    public void clearEvents() throws DataAccessException {
        String sqlString = "DELETE FROM event";
        if (accessCon == null) {
            return;
        }
        try (PreparedStatement stmt = accessCon.prepareStatement(sqlString)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    /** method gets the events in a user's family tree based on their username
     * @param username the string representing the user's username
     * @return list of user's events
     */
    public List<Event> getUserEvents(String username) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = accessCon.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }

    /** method gets the events for a given person in the tree
     * @param personID string identifying the person we're finding events for
     * @return a list of the person's life events
     */
    public List<Event> getPersonEvents(String personID) {
        return null;
    }

    /** method gets events that happened in a given country
     * @param country the name of the country
     * @return a list of events that took place in country
     */
    public List<Event> eventsByCountry(String country) {
        return null;
    }

    /** same as the country event, except with
     * @param city
     * @return list of events in a city
     */
    public List<Event> getEventsByCity(String city) {
        return null;
    }

    /** this time, we're returning events taking place in a specific location
     * @param latitude
     * @param longitude
     * @return list of events by location
     */
    public List<Event> getEventsByLocation(float latitude, float longitude) {
        return null;
    }

    /** @return list of events of a given type
     * @param eventType the type of event to be returned
     * @return a lsit of evetns that match the type
     */
    public List<Event> getEventsByType(String eventType) {
        return null;
    }

    /** @return list of events that happened in a given year
     * @param year the year to look for
     * @return a list of events by year
     */
    public List<Event> getEventsByYear(int year) {
        return null;
    }

    /** clears all events associated with a user
     * @param username user's username
     */
    public void removeEvents(String username) {
    }

}

