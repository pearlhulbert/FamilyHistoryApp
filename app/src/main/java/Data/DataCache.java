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
    private Map<Person, String> personFamilyRelationships;
    private Map<String, List<Person>> personFamily;
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

    public List<Event> getPersonsEventsById(String id) {
       return personEvents.get(id);
    }


    public Map<Person, String> getPersonFamilyRelationships() {
        return personFamilyRelationships;
    }

    public void setPersonFamilyRelationships(Map<Person, String> personFamilyRelationships) {
        this.personFamilyRelationships = personFamilyRelationships;
    }

    public Map<String, List<Person>> getPersonFamily() {
        return personFamily;
    }

    public void setPersonFamily(Map<String, List<Person>> personFamily) {
        this.personFamily = personFamily;
    }

    public void createPersonFamily() {
        if (!personFamilyRelationships.isEmpty() || !personFamily.isEmpty()) {
            personFamilyRelationships.clear();
        }
        List<Person> theFam = new ArrayList<>();
        if (currPerson.hasMother() || currPerson.hasSpouse()) {
            for (Person p: people.values()) {
                if (currPerson.hasMother()) {
                    if (p.getPersonID().equals(currPerson.getMotherID())) {
                        personFamilyRelationships.put(p,"Mother");
                        theFam.add(p);
                    }
                    if (p.getPersonID().equals(currPerson.getFatherID())) {
                        personFamilyRelationships.put(p, "Father");
                        theFam.add(p);
                    }
                }
                if (currPerson.hasSpouse()) {
                    if (p.getPersonID().equals(currPerson.getSpouseID())) {
                        personFamilyRelationships.put(p,"Spouse");
                        theFam.add(p);
                    }
                    if (p.getMotherID().equals(currPerson.getPersonID()) || p.getFatherID().equals(currPerson.getPersonID())) {
                        personFamilyRelationships.put(p,"Child");
                        theFam.add(p);
                    }
                }
            }
            personFamily.put(currPerson.getPersonID(), theFam);
        }

    }

}
