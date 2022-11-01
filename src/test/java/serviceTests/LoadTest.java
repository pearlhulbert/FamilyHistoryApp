package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.LoadResult;
import model.*;
import service.LoadSerivce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

    private LoadSerivce service;
    private PersonDAO pDAO;
    private EventDAO eDAO;
    private UserDAO uDAO;
    private LoadResult result;
    private User[] users;
    private Person[] persons;
    private Event[] events;
    private LoadRequest load;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        User user = new User("name", "word", "email", "fName", "lName", "g", "pid");
        User otherUser = new User("n", "w", "e", "f", "l", "g", "p");

        Person person = new Person("pid", "name", "fName", "lName", "g");
        Person otherPerson = new Person("idP", "n", "fName", "lName", "g");

        Event event = new Event("id", "name", "pid", 0, 0, "country", "city", "type", 1900);
        Event otherEvent = new Event("eID", "n", "idP", 0, 0, "country", "city", "type", 1900);

        users = new User[] {user, otherUser};

        persons = new Person[] {person, otherPerson};

        events = new Event[] {event, otherEvent};

        load = new LoadRequest(users, persons, events);
        service = new LoadSerivce();

        eDAO = new EventDAO(connection);
        pDAO = new PersonDAO(connection);
        uDAO = new UserDAO(connection);

    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void loadPass() throws DataAccessException {
        result = service.load(load);
        assertTrue(result.isSuccess());

        Person findPerson = pDAO.find(persons[0].getPersonID());
        Event findEvent = eDAO.find(events[0].getEventID());
        User findUser = uDAO.find(users[0].getUsername());

        assertNotNull(findPerson);
        assertEquals(persons[0], findPerson);

        assertNotNull(findEvent);
        assertEquals(events[0], findEvent);

        assertNotNull(findUser);
        assertEquals(users[0], findUser);
    }

    @Test
    public void loadFail() throws DataAccessException {
        users = null;
        persons = null;
        events = null;

        load = new LoadRequest(users, persons, events);
        result = service.load(load);
        assertFalse(result.isSuccess());
        String message = "Error: Nothing to load";
        assertEquals(message, result.getMessage());
    }
}
