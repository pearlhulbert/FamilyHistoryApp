package com.example.familymapclient;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;


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

    @Before
    public void setUp()  {
        loginRequest = new LoginRequest("username", "password");
        proxy = new ServerProxy("localhost", "8080");
        registerRequest = new RegisterRequest("username", "password",  "email", "firstName",
                "lastName", "gender");
        instance = DataCache.getInstance();
        registerResult = proxy.register(registerRequest);
    }

    @After
    public void cleanUp() {
        proxy.clear();
    }


    @Test
    public void registerPassOneUser() {
        assertTrue(registerResult.isSuccess());
        assertEquals(instance.getCurrPerson().getPersonID(), registerResult.getPersonID());
    }

    @Test
    public void registerPassTwoUsers() {
        RegisterRequest request = new RegisterRequest("user", "pass", "e", "fName",
                "lName", "g");
        RegisterResult secondResult = proxy.register(request);
        assertTrue(secondResult.isSuccess());
        assertEquals(instance.getCurrPerson().getPersonID(), secondResult.getPersonID());
    }

    @Test
    public void registerFail() {
        RegisterResult result = proxy.register(registerRequest);
        assertFalse(result.isSuccess());
    }

    @Test
    public void loginPass()  {
        LoginResult result = proxy.login(loginRequest);
        assertTrue(result.isSuccess());
        assertEquals(instance.getUserPersonId(), result.getPersonID());
        assertEquals(instance.getCurrAuthtoken(), result.getAuthtoken());
    }

    @Test
    public void loginFail()  {
        LoginRequest badRequest = new LoginRequest("bad", "bad");
        LoginResult result = proxy.login(badRequest);
        assertFalse(result.isSuccess());
    }

    @Test
    public void familyPass() {
        proxy.login(loginRequest);
        FamilyResult result = proxy.getFamily();
        assertTrue(result.isSuccess());
        assertNotNull(result.getFamily());
        assertNotNull(instance.getPeople());
        assertNotNull(instance.getCurrAuthtoken());
        int familySize = 31;
        assertEquals(familySize, result.getFamily().length);
    }

    @Test
    public void familyFail() {
        String goodToken = instance.getCurrAuthtoken();
        String badToken = "token";
        instance.setCurrAuthtoken(badToken);
        FamilyResult result = proxy.getFamily();
        assertNull(result);
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
    }

    @Test
    public void allEventFail() {
        String goodToken = instance.getCurrAuthtoken();
        String badToken = "token";
        instance.setCurrAuthtoken(badToken);
        AllEventResult result = proxy.getEvents();
        assertNull(result);
        instance.setCurrAuthtoken(goodToken);
    }

}
