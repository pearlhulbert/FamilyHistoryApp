package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import service.ClearService;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {

    private ClearService service;
    private PersonDAO pDAO;
    private EventDAO eDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private ClearResult result;
    private Event event;
    private Person person;
    private User user;
    private AuthToken token;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new ClearService();
        eDAO = new EventDAO(connection);
        pDAO = new PersonDAO(connection);
        uDAO = new UserDAO(connection);
        aDAO = new AuthTokenDAO(connection);

    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void clearHappyPath() throws DataAccessException {
        event = new Event("id", "name", "pid", 0, 0, "country", "city", "type", 1900);
        person = new Person("pid", "name", "fName", "lName", "g");
        user = new User("name", "word", "email", "fName", "lName", "g", "pid");
        token = new AuthToken("token", "name");

        eDAO.insert(event);
        pDAO.insert(person);
        uDAO.insert(user);
        aDAO.insert(token);

        result = service.clear();

        assertTrue(result.isSuccess());
        assertNull(eDAO.find(event.getEventID()));
        assertNull(pDAO.find(person.getPersonID()));
        assertNull(uDAO.find(user.getUsername()));
        assertNull(aDAO.find(token.getAuthtoken()));

    }

    @Test
    public void clearAlternatePath() throws DataAccessException {
        token = new AuthToken("weirdToken", "uName");
        person = new Person("idP", "name", "fName", "lName", "g");

        pDAO.insert(person);
        aDAO.insert(token);

        result = service.clear();
        assertTrue(result.isSuccess());
        assertNull(pDAO.find(person.getPersonID()));
        assertNull(aDAO.find(token.getAuthtoken()));
    }

}
