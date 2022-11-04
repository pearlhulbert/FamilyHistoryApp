package request;

public class FamilyRequest {
    private String authoken;

    public FamilyRequest(String authoken) {
        this.authoken = authoken;
    }

    public String getAuthoken() {
        return authoken;
    }

    public void setAuthoken(String authoken) {
        this.authoken = authoken;
    }
}
