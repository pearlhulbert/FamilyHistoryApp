package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.AllEventResult;
import result.RegisterResult;
import service.AllEventService;
import service.ClearService;
import model.*;
import service.RegisterService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AllEventTest {

    private AllEventService service;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private AllEventResult result;
    private RegisterResult registerResult;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new AllEventService();

        uDAO = new UserDAO(connection);
        aDAO = new AuthTokenDAO(connection);

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
    public void allEventPass() throws DataAccessException {
        result = service.allEvents(registerResult.getAuthtoken());
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        AuthToken token = aDAO.find(registerResult.getAuthtoken());
        assertNotNull(uDAO.find(token.getUsername()));
        assertEquals(uDAO.find(token.getUsername()), uDAO.find(registerResult.getUsername()));
        int eventSize = 91;
        assertEquals(eventSize, (result.getData()).length);
    }

    @Test
    public void allEventFail() throws DataAccessException {
        result = service.allEvents("badToken");
        assertFalse(result.isSuccess());
        assertNull(aDAO.find("badToken"));
        String message = "Error: Invalid authtoken/authtoken not found";
        assertEquals(message, result.getMessage());
    }
}
