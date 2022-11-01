package daoTests;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        if (eDao != null) {
            eDao.clearEvents();
        }
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(bestEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(bestEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(bestEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        Event otherEvent = new Event("eID", "uName", "pId", 0f, 0f, "country", "city", "type", 1988);
        eDao.insert(bestEvent);
        eDao.insert(otherEvent);
        Event compEvent = eDao.find(bestEvent.getEventID());
        Event nextEvent = eDao.find(otherEvent.getEventID());
        assertEquals(bestEvent, compEvent);
        assertEquals(otherEvent, nextEvent);
    }

    @Test
    public void findFail() throws DataAccessException {
        Event fakeEvent = new Event("e", "a", "p", 0, 0, "c", "ci", "et", 1970);
        eDao.find(fakeEvent.getEventID());
        assertNull(eDao.find(fakeEvent.getEventID()));
    }

    @Test
    public void clearTest() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.clearEvents();
        assertNull(eDao.find(bestEvent.getEventID()));
    }

    @Test
    public void removePass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event otherEvent = new Event("e", "a", "p", 0, 0, "c", "ci", "et", 1970);
        eDao.insert(otherEvent);

        eDao.remove(otherEvent.getAssociateUserName());
        assertNull(eDao.find(otherEvent.getEventID()));
        Event findEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(findEvent);
        assertEquals(bestEvent, findEvent);
    }

    @Test
    public void removeFail() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.remove(bestEvent.getEventID());

        Event findEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(findEvent);
        assertEquals(bestEvent, findEvent);
    }

}

