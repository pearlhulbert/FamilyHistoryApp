package ServerProxyTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import Data.DataCache;
import Proxy.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.FamilyResult;
import result.LoginResult;
import result.RegisterResult;


public class ProxyTest {

    private LoginRequest loginRequest;
    private ServerProxy proxy;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;
    private DataCache instance;

    @BeforeEach
    public void setUp()  {
        loginRequest = new LoginRequest("username", "password");
        proxy = new ServerProxy("localhost", "8080");
        registerRequest = new RegisterRequest("username", "password",  "email", "firstName",
                "lastName", "gender");
        instance = DataCache.getInstance();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void registerPassOneUser() {
        registerResult = proxy.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        assertNotNull(instance.getPersonById(registerResult.getPersonID()));
        assertEquals(instance.getPersonById(registerResult.getPersonID()).getFirstName(), registerRequest.getFirstName());
        assertEquals(instance.getPersonById(registerResult.getPersonID()).getLastName(), registerRequest.getLastName());
        assertEquals(instance.getPersonById(registerResult.getPersonID()).getGender(), registerRequest.getGender());
    }

    @Test
    public void registerPassTwoUsers() {
        RegisterRequest request = new RegisterRequest("user", "pass", "e", "fName",
                "lName", "g");
        RegisterResult secondResult = proxy.register(request);
        assertTrue(secondResult.isSuccess());
        assertNotNull(instance.getPersonById(secondResult.getPersonID()));
        assertEquals(instance.getPersonById(secondResult.getPersonID()).getFirstName(), request.getFirstName());
        assertEquals(instance.getPersonById(secondResult.getPersonID()).getLastName(), request.getLastName());
        assertEquals(instance.getPersonById(secondResult.getPersonID()).getGender(), request.getGender());

    }

    @Test
    public void registerFail() {
        proxy.register(registerRequest);
        RegisterResult result = proxy.register(registerRequest);
        assertFalse(result.isSuccess());
    }

    @Test
    public void loginPass()  {
        registerResult = proxy.register(registerRequest);
        LoginResult result = proxy.login(loginRequest);
        assertTrue(result.isSuccess());
        assertNotNull(instance.getPersonById(result.getPersonID()));
        assertEquals(instance.getPersonById(result.getPersonID()).getAssociatedUsername(), loginRequest.getUsername());
        assertEquals(result.getPersonID(), registerResult.getPersonID());
    }

    @Test
    public void loginFail()  {
        LoginRequest badRequest = new LoginRequest("bad", "bad");
        LoginResult result = proxy.login(badRequest);
        assertFalse(result.isSuccess());
    }

    @Test
    public void familyPass() {
        registerResult = proxy.register(registerRequest);
        LoginResult loginResult = proxy.login(loginRequest);
        FamilyResult result = proxy.getFamily();
        assertTrue(result.isSuccess());
        assertNotNull(result.getFamily());
        assertNotNull(instance.getPeople());
        assertNotNull(instance.getCurrAuthtoken());
        int familySize = 31;
        assertEquals(familySize, result.getFamily().length);
        boolean familyMatch = true;
        for (int i = 0; i < result.getFamily().length; ++i) {
            if (instance.getPersonById(result.getFamily()[i].getPersonID()) == null) {
                familyMatch = false;
            }
        }
        assertTrue(familyMatch);
    }

    @Test
    public void familyFail() {
        String goodToken = instance.getCurrAuthtoken();
        String badToken = "token";
        instance.setCurrAuthtoken(badToken);
        FamilyResult result = proxy.getFamily();
        assertFalse(result.isSuccess());
        instance.setCurrAuthtoken(goodToken);
    }

    @Test
    public void allEventPass() {
        registerResult = proxy.register(registerRequest);
        LoginResult loginResult = proxy.login(loginRequest);
        AllEventResult result = proxy.getEvents();
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertNotNull(instance.getEvents());
        assertNotNull(instance.getCurrAuthtoken());
        int eventSize = 91;
        assertEquals(eventSize, result.getData().length);
        boolean eventMatch = true;
        for (int i = 0; i < result.getData().length; ++i) {
            if (instance.getEventById(result.getData()[i].getEventID()) == null) {
                eventMatch = false;
            }
        }
        assertTrue(eventMatch);
    }

    @Test
    public void allEventFail() {
        String goodToken = instance.getCurrAuthtoken();
        String badToken = "token";
        instance.setCurrAuthtoken(badToken);
        AllEventResult result = proxy.getEvents();
        assertFalse(result.isSuccess());
        instance.setCurrAuthtoken(goodToken);
    }

}
