package daoTests;

import dao.DataAccessException;
import dao.Database;
import dao.AuthTokenDAO;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken newToken;
    private AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newToken = new AuthToken("token", "username");

        Connection conn = db.getConnection();

        if (aDao != null) {
            aDao.clearTokens();
        }

        aDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(newToken);
        AuthToken compareTest = aDao.find(newToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(newToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        aDao.insert(newToken);
        assertThrows(DataAccessException.class, () -> aDao.insert(newToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        AuthToken otherToken = new AuthToken("aToken", "uName");
        aDao.insert(newToken);
        aDao.insert(otherToken);
        AuthToken compToken = aDao.find(newToken.getAuthtoken());
        AuthToken nextToken = aDao.find(otherToken.getAuthtoken());
        assertEquals(newToken, compToken);
        assertEquals(otherToken, nextToken);
    }

    @Test
    public void findFail() throws DataAccessException {
        AuthToken fakeToken = new AuthToken("a", "u");
        aDao.find(fakeToken.getAuthtoken());
        assertNull(aDao.find(fakeToken.getAuthtoken()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        aDao.insert(newToken);
        aDao.clearTokens();
        assertNull(aDao.find(newToken.getAuthtoken()));
    }
}
