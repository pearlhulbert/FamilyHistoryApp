package daoTests;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person newPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newPerson = new Person("pID", "aUName", "first", "last", "g");

        Connection conn = db.getConnection();
        pDao = new PersonDAO(conn);
        pDao.clearPeople();
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //Make sure that elements are inserted properly
        pDao.insert(newPerson);
        Person compPerson = pDao.find(newPerson.getPersonID());
        assertNotNull(compPerson);
        assertEquals(newPerson, compPerson);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //Ensures that trying to add a duplicate fails
        pDao.insert(newPerson);
        assertThrows(DataAccessException.class, () -> pDao.insert(newPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        //add multiple items, see if they are both found
        Person otherPerson = new Person("person", "username",
                "fName", "lName", "gender");
        pDao.insert(newPerson);
        pDao.insert(otherPerson);
        Person findPerson = pDao.find(newPerson.getPersonID());
        Person compPerson = pDao.find(otherPerson.getPersonID());
        assertNotNull(findPerson);
        assertNotNull(compPerson);
        assertEquals(newPerson, findPerson);
        assertEquals(otherPerson, compPerson);
    }

    @Test
    public void findFail() throws DataAccessException {
        Person fakePerson = new Person("p", "a", "f", "l", "g");
        pDao.find(fakePerson.getPersonID());
        assertNull(pDao.find(fakePerson.getPersonID()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        pDao.insert(newPerson);
        pDao.clearPeople();
        assertNull(pDao.find(newPerson.getPersonID()));
    }

    @Test
    public void removePass() throws DataAccessException {
        pDao.insert(newPerson);
        Person otherPerson = new Person("p", "a", "f", "l", "g");
        pDao.insert(otherPerson);

        pDao.remove(otherPerson.getAssociatedUsername());
        assertNull(pDao.find(otherPerson.getPersonID()));
        Person findPerson = pDao.find(newPerson.getPersonID());
        assertNotNull(findPerson);
        assertEquals(newPerson, findPerson);
    }

    @Test
    public void removeFail() throws DataAccessException {
        pDao.insert(newPerson);
        pDao.remove(newPerson.getPersonID());

        Person findPerson = pDao.find(newPerson.getPersonID());
        assertNotNull(findPerson);
        assertEquals(newPerson, findPerson);
    }
}
