package com.example.familymapclient;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import Data.DataCache;
import Proxy.ServerProxy;
import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.FamilyResult;
import result.LoginResult;
import result.RegisterResult;

public class DataCacheTest {

    private RegisterResult registerResult;
    private ServerProxy proxy;
    private LoginResult loginResult;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private DataCache instance;

    @Before
    public void setUp() {
        instance = DataCache.getInstance();
        proxy = new ServerProxy("localhost", "8080");
        loginRequest = new LoginRequest("username", "password");
        registerRequest = new RegisterRequest("username", "password", "email", "firstName",
                "lastName", "gender");
        registerResult = proxy.register(registerRequest);
        loginResult = proxy.login(loginRequest);
        FamilyResult familyResult = proxy.getFamily();
        AllEventResult eventResult = proxy.getEvents();
        instance.addPeopleToCache(familyResult.getFamily());
        instance.addEventsToCache(eventResult.getData());
    }

    @After
    public void cleanUp() {
        proxy.clear();
    }

    @Test
    public void famCreateHappyPath() {
        instance.createPersonFamily();
        assertTrue(!instance.getPersonFamily().isEmpty());
        assertTrue(!instance.getPersonFamilyRelationships().isEmpty());
        assertTrue(instance.getPersonFamily().containsKey(instance.getCurrPerson().getPersonID()));
        Person mother = instance.getPersonById(instance.getCurrPerson().getMotherID());
        Person father = instance.getPersonById(instance.getCurrPerson().getFatherID());
        assertTrue(instance.getPersonFamilyRelationships().containsKey(mother));
        assertTrue(instance.getPersonFamilyRelationships().containsKey(father));
        assertEquals("Mother", instance.getPersonFamilyRelationships().get(mother));
        assertEquals("Father", instance.getPersonFamilyRelationships().get(father));
    }

    @Test
    public void famCreateAlternatePath() {
        instance.setCurrPerson(instance.getPersonById(instance.getCurrPerson().getMotherID()));
        instance.createPersonFamily();
        assertTrue(!instance.getPersonFamily().isEmpty());
        assertTrue(!instance.getPersonFamilyRelationships().isEmpty());
        assertTrue(instance.getPersonFamily().containsKey(instance.getCurrPerson().getPersonID()));
        Person mother = instance.getPersonById(instance.getCurrPerson().getMotherID());
        Person father = instance.getPersonById(instance.getCurrPerson().getFatherID());
        Person spouse = instance.getPersonById(instance.getCurrPerson().getSpouseID());
        assertTrue(instance.getPersonFamilyRelationships().containsKey(mother));
        assertTrue(instance.getPersonFamilyRelationships().containsKey(father));
        assertEquals("Mother", instance.getPersonFamilyRelationships().get(mother));
        assertEquals("Father", instance.getPersonFamilyRelationships().get(father));
        assertEquals("Spouse", instance.getPersonFamilyRelationships().get(spouse));
    }

    @Test
    public void searchTestOneLetter() {
        List<Person> people = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        String searchString = "f";
        int personNum = 1;
        int eventNum = 2;
        instance.search(searchString, events, people);
        assertFalse(events.isEmpty());
        assertFalse(people.isEmpty());
        assertTrue(events.size() >= eventNum);
        assertTrue(people.size() >= personNum);
        assertTrue(people.contains(instance.getCurrPerson()));
    }

    @Test
    public void searchTestFirstName() {
        List<Person> people = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        String searchString = "firstName";
        int personNum = 1;
        int eventNum = 1;
        instance.search(searchString, events, people);
        assertFalse(events.isEmpty());
        assertFalse(people.isEmpty());
        System.out.print(events.size());
        assertTrue(events.size() >= eventNum);
        assertTrue(people.size() >= personNum);
        assertTrue(people.contains(instance.getCurrPerson()));
    }
}
