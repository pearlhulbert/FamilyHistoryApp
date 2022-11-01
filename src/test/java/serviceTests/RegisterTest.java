package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.RegisterRequest;
import result.LoadResult;
import model.*;
import result.RegisterResult;
import service.ClearService;
import service.RegisterService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private RegisterService service;
    private PersonDAO pDAO;
    private EventDAO eDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private RegisterResult result;
    private RegisterRequest request;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new RegisterService();

        eDAO = new EventDAO(connection);
        pDAO = new PersonDAO(connection);
        uDAO = new UserDAO(connection);
        aDAO = new AuthTokenDAO(connection);

        request = new RegisterRequest("username", "password", "email", "first", "last", "g");

    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void registerPass() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
        result = service.registerUser(request);
        assertTrue(result.isSuccess());
        User registerUser = uDAO.find(result.getUsername());
        User findUser = uDAO.find("username");
        assertNotNull(registerUser);
        assertNotNull(result.getAuthtoken());
        assertEquals(result.getUsername(), aDAO.find(result.getAuthtoken()).getUsername());
        assertEquals(registerUser, findUser);
    }

    @Test
    public void registerFail() throws DataAccessException {
        RegisterRequest newRequest = new RegisterRequest("username", "password", "email", "fName","lName", "g");
        result = service.registerUser(newRequest);
        assertFalse(result.isSuccess());
        String message = "Error: Error encountered while inserting into the database";
        assertEquals(message, result.getMessage());
    }

}
