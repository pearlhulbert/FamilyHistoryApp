package result;

public class LoginResult {
    private String username;
    private String personID;
    private String authtoken;
    private String message;
    private boolean success;

    public LoginResult(String username, String personID, String authtoken, boolean success) {
        this.username = username;
        this.personID = personID;
        this.authtoken = authtoken;
        this.success = success;
    }

    public LoginResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
