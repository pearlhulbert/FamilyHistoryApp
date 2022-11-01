package service;

import dao.*;
import request.LoginRequest;
import result.LoadResult;
import result.LoginResult;
import java.util.UUID;
import model.User;
import java.sql.Connection;
import model.AuthToken;

public class LoginService {

    /** method takes info from the login request and logs the user in while returning an authtoken
     * @param log the request body
     * @return the result, which includes an authtoken
     */
    public LoginResult login(LoginRequest log) {
        Database db = new Database();
        String message;

        boolean success = false;
        try {
            Connection connect = db.openConnection();
            UUID random = UUID.randomUUID();
            String token = random.toString();
            String uName = log.getUsername();

            AuthToken authToken = new AuthToken(token, uName);
            AuthTokenDAO aDAO = new AuthTokenDAO(connect);
            aDAO.insert(authToken);

            UserDAO userDAO = new UserDAO(connect);
            User currUser = userDAO.find(uName);
            if (currUser == null) {
                throw new DataAccessException("User not found in database");
            }
            else if (!(currUser.getPassword()).equals(log.getPassword())) {
                throw new DataAccessException("Entered invalid password");
            }
            String pID = currUser.getPersonID();

            success = true;
            LoginResult logR = new LoginResult(uName, pID, token, success);
            db.closeConnection(true);
            return logR;
        } catch (DataAccessException d) {
            d.printStackTrace();
            message = "Error: " + d.getMessage();
            success = false;
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                System.out.println("No close login");
                e.printStackTrace();
            }
            LoginResult logR = new LoginResult(message, success);
            return logR;
        }
    }
}
