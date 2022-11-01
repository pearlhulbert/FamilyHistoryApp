package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import model.*;
import result.LoginResult;
import service.LoginService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private LoginService service;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private LoginResult result;
    private LoginRequest request;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new LoginService();

        uDAO = new UserDAO(connection);
        aDAO = new AuthTokenDAO(connection);

        request = new LoginRequest("username", "password");

    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void loginPass() throws DataAccessException {
        result = service.login(request);
        assertTrue(result.isSuccess());
        User registerUser = uDAO.find(result.getUsername());
        User findUser = uDAO.find("username");
        assertNotNull(registerUser);
        assertNotNull(result.getAuthtoken());
        assertEquals(result.getUsername(), aDAO.find(result.getAuthtoken()).getUsername());
        assertEquals(registerUser, findUser);
    }

    @Test
    public void loginFail() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest(null, "word");
        result = service.login(loginRequest);
        assertFalse(result.isSuccess());
        String message = "Error: Error encountered while inserting into the database";
        assertEquals(message, result.getMessage());
    }

}
