package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.EventResult;
import result.RegisterResult;
import service.ClearService;
import service.EventService;
import service.RegisterService;
import model.*;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private EventService service;
    private EventDAO eDAO;
    private EventResult result;
    private RegisterResult registerResult;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new EventService();
        eDAO = new EventDAO(connection);

        ClearService clearService = new ClearService();
        clearService.clear();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email", "first", "last", "g");
        RegisterService registerService = new RegisterService();
        registerResult = registerService.registerUser(registerRequest);

    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void eventPass() throws DataAccessException {
        List<Event> events = eDAO.getUserEvents(registerResult.getUsername());
        Event[] eventArray = new Event[events.size()];
        eventArray = events.toArray(eventArray);
        Event currEvent = eventArray[0];
        String eventID = currEvent.getEventID();
        result = service.event(registerResult.getAuthtoken(), eventID);
        assertTrue(result.isSuccess());
        assertNotNull(eDAO.find(currEvent.getEventID()));
        assertEquals((eDAO.find(currEvent.getEventID())).getAssociateUserName(), result.getUsername());
    }

    @Test
    public void eventFailBadAuth() throws DataAccessException {
        result = service.event("werido", registerResult.getPersonID());
        assertFalse(result.isSuccess());
        String message = "Error: Authtoken invalid";
        assertEquals(message, result.getMessage());
    }

    @Test
    public void eventFailBadEvent() throws DataAccessException {
        result = service.event(registerResult.getAuthtoken(), "id");
        assertFalse(result.isSuccess());
        String message = "Error: Event with specified id not found in database";
        assertEquals(message, result.getMessage());
    }
}
