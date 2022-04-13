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
        this.personFamilyRelationships = new HashMap<>();
        this.personFamily = new HashMap<>();
        this.males = new ArrayList<>();
        this.females = new ArrayList<>();
        this.momSide = new ArrayList<>();
        this.dadSide = new ArrayList<>();
        this.maleFilter = false;
        this.femaleFilter = false;
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Map<Person, String> personFamilyRelationships;
    private Map<String, List<Person>> personFamily;
    private List<Person> males;
    private List<Person> females;
    private List<Person> momSide;
    private List<Person> dadSide;
    private Person currPerson;
    private String currAuthtoken;
    private String userPersonId;
    private Event currEvent;
    private boolean femaleFilter;
    private boolean hasFilter;

    public boolean isFemaleFilter() {
        return femaleFilter;
    }

    public void setFemaleFilter(boolean femaleFilter) {
        this.femaleFilter = femaleFilter;
    }

    public boolean isMaleFilter() {
        return maleFilter;
    }

    public void setMaleFilter(boolean maleFilter) {
        this.maleFilter = maleFilter;
    }

    private boolean maleFilter;

    public Event getCurrEvent() {
        return currEvent;
    }

    public void setCurrEvent(Event currEvent) {
        this.currEvent = currEvent;
    }

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

    public void filterByGender() {
        for (Person p: people.values()) {
            if (p.getGender().equals("m")) {
                males.add(p);
            }
            else {
                females.add(p);
            }
        }
    }

    public List<Event> getMaleEvents() {
        List<Event> maleEvents = new ArrayList<>();
        for (Event e : events.values()) {
            for (Person p: males) {
                if (e.getPersonId().equals(p.getPersonID())) {
                    maleEvents.add(e);
                }
            }
        }
        return maleEvents;
    }

    public List<Person> getMales() {
        return males;
    }

    public void setMales(List<Person> males) {
        this.males = males;
    }

    public List<Person> getFemales() {
        return females;
    }

    public void setFemales(List<Person> females) {
        this.females = females;
    }

    public List<Person> getMomSide() {
        return momSide;
    }

    public void setMomSide(List<Person> momSide) {
        this.momSide = momSide;
    }

    public List<Person> getDadSide() {
        return dadSide;
    }

    public void setDadSide(List<Person> dadSide) {
        this.dadSide = dadSide;
    }

    public List<Event> getFemaleEvents() {
        List<Event> femaleEvents = new ArrayList<>();
        for (Event e : events.values()) {
            for (Person p: females) {
                if (e.getPersonId().equals(p.getPersonID())) {
                    femaleEvents.add(e);
                }
            }
        }
        return femaleEvents;
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
                    if (p.hasMother()) {
                        if (p.getMotherID().equals(currPerson.getPersonID()) || p.getFatherID().equals(currPerson.getPersonID())) {
                            personFamilyRelationships.put(p,"Child");
                            theFam.add(p);
                        }
                    }
                }
            }
            personFamily.put(currPerson.getPersonID(), theFam);
        }
    }

}
