package serviceTests;

import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.FamilyResult;
import result.RegisterResult;
import service.ClearService;
import service.FamilySerivce;
import model.*;
import service.RegisterService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyTest {

    private FamilySerivce service;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private FamilyResult result;
    private RegisterResult registerResult;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        connection = DriverManager.getConnection("jdbc:sqlite:famDB.db");

        service = new FamilySerivce();

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
    public void familyPass() throws DataAccessException {
        result = service.family(registerResult.getAuthtoken());
        assertTrue(result.isSuccess());
        assertNotNull(result.getFamily());
        AuthToken token = aDAO.find(registerResult.getAuthtoken());
        assertNotNull(uDAO.find(token.getUsername()));
        assertEquals(uDAO.find(token.getUsername()), uDAO.find(registerResult.getUsername()));
        int familySize = 31;
        assertEquals(familySize, (result.getFamily()).length);
    }

    @Test
    public void familyFail() throws DataAccessException {
        result = service.family("badToken");
        assertFalse(result.isSuccess());
        assertNull(aDAO.find("badToken"));
        String message = "Error: Invalid authtoken/authtoken not found";
        assertEquals(message, result.getMessage());
    }
}
