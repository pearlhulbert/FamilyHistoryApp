package dao;

import model.AuthToken;
import model.Event;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AuthTokenDAO {
    private final Connection accessCon;

    public AuthTokenDAO(Connection con) {this.accessCon = con;}

    /** method used to insert an authtoken object into the database table using sql commands as string
     * @param token is the event object to be inserted
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sqlString = "INSERT INTO authtoken (authtoken, username) VALUES(?,?)";

        try (PreparedStatement stmt = accessCon.prepareStatement(sqlString)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, token.getAuthtoken());
            stmt.setString(2, token.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** method used to find an authoken in the database based on the token string.
     * These commands will also be in SQL
     * @param token is a unique key used to authenticate a user
     * @return the token matching this token string
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken aToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtoken WHERE authtoken = ?;";
        try (PreparedStatement stmt = accessCon.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                aToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return aToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /** this method removes a token from the database table
     * @param token token to be removed
     * @return nothing
     */
    public void remove(AuthToken token) throws DataAccessException {
    }

    /** this method searches the database for the token that belongs with this username (if it exists)
     * @param username the current user's username
     * @param token the current user's authtoken
     * @return the confimred user
     */
    public User matchToken(String username, String token) {
        return null;
    }

    public void clearTokens() throws DataAccessException {
        String sqlString = "DELETE FROM authtoken";
        if (accessCon == null) {
            return;
        }
        try (PreparedStatement stmt = accessCon.prepareStatement(sqlString)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
