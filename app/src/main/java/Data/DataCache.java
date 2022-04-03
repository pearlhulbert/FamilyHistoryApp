package Data;
import model.*;
import java.util.*;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
        this.people = new HashMap<>();
        this.events = new HashMap<>();
        this.personEvents = new HashMap<>();
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Map<String, Set<Person>> momSide;
    private Map<String, Set<Person>> dadSide;
    private Person currPerson;
    private String currAuthtoken;
    private String userPersonId;

    public String getUserPersonId() {
        return userPersonId;
    }

    public void setUserPersonId(String userPersonId) {
        this.userPersonId = userPersonId;
    }

    public void setCurrPerson(Person currPerson) {
        this.currPerson = currPerson;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }

    public String getCurrAuthtoken() {
        return currAuthtoken;
    }

    public void setCurrAuthtoken(String currAuthtoken) {
        this.currAuthtoken = currAuthtoken;
    }

    public Person getCurrPerson() {
        return currPerson;
    }

    public Person getPersonById(String id) {
        return people.get(id);
    }

    public Event getEventById(String id) {
       return events.get(id);
    }

    public List<Event> getPersonsEvents(String id) {
       return personEvents.get(id);
    }

}
