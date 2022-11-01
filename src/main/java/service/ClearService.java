package service;

import dao.*;
import result.ClearResult;

import java.sql.Connection;

public class ClearService {

    /** clears the family tree/database
     * @return the result of the clearing
     */
    public ClearResult clear() throws DataAccessException {
        Database db = new Database();
        String respMessage;
        boolean success = false;

        try {
            //open
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);
            EventDAO eDAO = new EventDAO(connection);
            PersonDAO pDAO = new PersonDAO(connection);
            AuthTokenDAO aDAO = new AuthTokenDAO(connection);

            eDAO.clearEvents();
            pDAO.clearPeople();
            uDAO.clearUsers();
            aDAO.clearTokens();

            //close
            db.closeConnection(true);

            respMessage = "Clear succeeded.";
            success = true;

        }
        catch (DataAccessException d) {
            d.printStackTrace();
            respMessage = "Error: " + d.getMessage();
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("No close clear");
                e.printStackTrace();
            }
        }

        ClearResult clearResult = new ClearResult(respMessage, success);
        return clearResult;
    }
}
