package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.AuthToken;
import model.Person;
import java.util.List;
import request.FamilyRequest;
import result.FamilyResult;
import result.PersonResult;

import java.sql.Connection;

public class FamilySerivce {

    /** method returns all family members of the current user, determined by authtoken in request body
     * @param token request containing authotken
     * @return a person array stored in a result of all family members of the user
     */
    public FamilyResult family(String token) {
        boolean success = false;
        String message;
        FamilyResult result = new FamilyResult();
        Database db = new Database();
        try {
            //open
            Connection connection = db.openConnection();

            AuthTokenDAO aDAO = new AuthTokenDAO(connection);
            AuthToken aToken = aDAO.find(token);
            if (aToken == null) {
                throw new DataAccessException("Invalid authtoken/authtoken not found");
            }
            String userName = aToken.getUsername();

            PersonDAO pDAO = new PersonDAO(connection);
            List<Person> people = pDAO.getFamily(userName);
            Person[] personArray = new Person[people.size()];
            personArray = people.toArray(personArray);
            //close
            db.closeConnection(true);
            success = true;
            result = new FamilyResult(personArray, success);
            System.out.println("result");
            return result;
        } catch(DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                System.out.println("No close family");
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            result = new FamilyResult(message, success);
            return result;
        }
    }

}
