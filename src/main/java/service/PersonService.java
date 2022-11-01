package service;

import dao.*;
import model.AuthToken;
import model.Person;
import dao.DataAccessException;
import request.PersonRequest;
import result.FillResult;
import result.PersonResult;

import java.sql.Connection;

public class PersonService {

    /** method returns a person object with the specified personID (contained in the request body)
     * @param token the request contianing the personID
     * @param pID
     * @return the person associated with the personID
     */
    public PersonResult person(String token, String pID) {
        boolean success = false;
        String message;
        Database db = new Database();
        PersonResult result = new PersonResult();
        try {
            Connection connection = db.openConnection();
            PersonDAO pDAO = new PersonDAO(connection);
            AuthTokenDAO aDAO = new AuthTokenDAO(connection);
            AuthToken a = aDAO.find(token);
            if (a == null) {
                throw new DataAccessException("Authtoken invalid");
            }
            String userName = a.getUsername();
            Person person = pDAO.find(pID);
            if (person == null) {
                throw new DataAccessException("Person with specified id not found in database");
            }
            if (!userName.equals(person.getAssociatedUsername())) {
                throw new DataAccessException("Authtoken not associated with username");
            }
            db.closeConnection(true);
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String gender = person.getGender();
            String motherID = "";
            String fatherID = "";
            String spouseID = "";
            success = true;
            if (person.hasFather() || person.hasMother() || person.hasSpouse()) {
                if (person.hasFather()) {
                    fatherID = person.getFatherID();
                }
                if (person.hasMother()) {
                    motherID = person.getMotherID();
                }
                if (person.hasSpouse()) {
                    spouseID = person.getSpouseID();
                }
                if (person.hasSpouse() && !person.hasMother()) {
                    result = new PersonResult(userName, pID, firstName, lastName, gender, spouseID, success);
                }
               else if (!person.hasSpouse() && person.hasMother()) {
                    result = new PersonResult(userName, pID, firstName, lastName, gender, fatherID, motherID, success);
                }
                else if (person.hasMother() && person.hasSpouse()) {
                    result = new PersonResult(userName, pID, firstName, lastName, gender, fatherID, motherID, spouseID, success);
                }
            }
            else {
                result = new PersonResult(userName, pID, firstName, lastName, gender, success);
            }
            return result;
        } catch(DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("No close person");
                e.printStackTrace();
            }
            result = new PersonResult(message, success);
            return result;
        }
    }
}
