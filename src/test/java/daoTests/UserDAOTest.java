package daoTests;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User newUser;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newUser = new User("username", "password", "email", "first", "last",
                "g", "pID");

        Connection conn = db.getConnection();
        if (uDao != null) {
            uDao.clearUsers();
        }
        uDao = new UserDAO(conn);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //Make sure that elements are inserted properly
        uDao.insert(newUser);
        User compUser = uDao.find(newUser.getUsername());
        assertNotNull(compUser);
        assertEquals(newUser, compUser);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //Ensures that trying to add a duplicate fails
        uDao.insert(newUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(newUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        //add multiple items, see if they are both found
        User otherUser = new User("uName", "pWord", "email@email.com",
                "fName", "lName", "gender", "person");
        uDao.insert(newUser);
        uDao.insert(otherUser);
        User findUser = uDao.find(newUser.getUsername());
        User compUser = uDao.find(otherUser.getUsername());
        assertNotNull(findUser);
        assertNotNull(compUser);
        assertEquals(newUser, findUser);
        assertEquals(otherUser, compUser);
    }

    @Test
    public void findFail() throws DataAccessException {
        User fakeUser = new User("u","p", "e", "f", "l","g","p");
        uDao.find(fakeUser.getUsername());
        assertNull(uDao.find(fakeUser.getUsername()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        uDao.insert(newUser);
        uDao.clearUsers();
        assertNull(uDao.find(newUser.getUsername()));
    }

}
