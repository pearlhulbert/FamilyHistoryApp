package service;

import dao.*;
import request.FillRequest;
import result.FillResult;
import model.Person;
import model.User;
import model.Event;

import java.util.*;
import java.sql.Connection;

public class FillService {

    /** method fills the family tree according to the specified username and potential number of generations
     * @param fRequest the request with said required info
     * @return the result of that request
     */
   public FillResult fill(FillRequest fRequest) {
        String userName = fRequest.getUsername();
        int generations = fRequest.getGenerations();
        FamilyTree newTree = new FamilyTree();
        String message;
        boolean success = false;
        Vector<Event> events = new Vector<>();
        Vector<Person> people = new Vector<>();
        User rootUser = new User();
       Database db = new Database();
       try {
           //open
           Connection connect = db.openConnection();

           UserDAO uDAO = new UserDAO(connect);
           PersonDAO pDAO = new PersonDAO(connect);
           EventDAO eDAO = new EventDAO(connect);

           rootUser = uDAO.find(userName);

           if (rootUser == null) {
               throw new DataAccessException("User does not exist in database");
           }

           pDAO.remove(userName);
           eDAO.remove(userName);

           newTree.generateRoot(userName, generations, events, people, rootUser);

           for (Person p : people) {
               if (!p.hasSpouse()) {
                   p.setFirstName(rootUser.getFirstName());
                   p.setLastName(rootUser.getLastName());
                   p.setPersonID(rootUser.getPersonID());
               }
               pDAO.insert(p);
           }
           for (Event e : events) {
               if (e.getYear() == 1700) {
                   e.setPersonId(rootUser.getPersonID());
               }
               eDAO.insert(e);
           }
           //close
           db.closeConnection(true);

           message = "Successfully added " + people.size() + " persons and " + events.size() + " events.";
           success = true;
           FillResult result = new FillResult(message, success);
           return result;
       } catch(DataAccessException d) {
           d.printStackTrace();
           message = "Error: " + d.getMessage();
           try {
               //close
               db.closeConnection(false);
           } catch (DataAccessException e) {
               System.out.println("No close fill");
               e.printStackTrace();
           }
           FillResult result = new FillResult(message, success);
           return result;
       }
    }

}
