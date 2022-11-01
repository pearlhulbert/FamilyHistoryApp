package service;

import dao.*;
import model.Person;
import request.LoadRequest;
import result.LoadResult;
import model.User;
import model.Event;

import java.sql.Connection;

public class LoadSerivce {

    /** method clears data from database and loads specified user, person, and event data from request
     * @param l request containing info to load
     * @return result of the load
     */
    public LoadResult load(LoadRequest l) {
        Database db = new Database();
        String respMessage;

        boolean success = false;

        try {
            Connection connect = db.openConnection();
            UserDAO uDAO = new UserDAO(connect);
            EventDAO eDAO = new EventDAO(connect);
            PersonDAO pDAO = new PersonDAO(connect);

            uDAO.clearUsers();
            eDAO.clearEvents();
            pDAO.clearPeople();

            if (l.getUsers() == null || l.getEvents() == null || l.getPersons() == null) {
                throw new DataAccessException("Nothing to load");
            }

            for (User u : l.getUsers()) {
                uDAO.insert(u);
            }
            for (Event e : l.getEvents()) {
                eDAO.insert(e);
            }
            for (Person p : l.getPersons()) {
                pDAO.insert(p);
            }
            db.closeConnection(true);

            respMessage = "Successfully added " + l.getUsers().length + " users, " +
            l.getPersons().length + " persons, and " + l.getEvents().length + "events to the database.";
            success = true;
            LoadResult loadR = new LoadResult(respMessage, success);
            return loadR;
        }
        catch (DataAccessException d) {
            d.printStackTrace();
            respMessage = "Error: " + d.getMessage();
            success = false;
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("No close load");
                e.printStackTrace();
            }
            LoadResult loadR = new LoadResult(respMessage, success);
            return loadR;
        }

    }
}
