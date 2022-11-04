package request;

public class EventRequest {
    private String eventID;
    private String authoken;

    public EventRequest(String eventID, String authoken) {
        this.eventID = eventID;
        this.authoken = authoken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthoken() {
        return authoken;
    }

    public void setAuthoken(String authoken) {
        this.authoken = authoken;
    }
}
