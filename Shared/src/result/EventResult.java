package result;

public class EventResult {

    private String associatedUsername;
    private String eventID;
    private String personID;
    private float latitude;
    private float longitude;

    public EventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    private String country;
    private String city;
    private String eventType;
    private int year;
    private String message;
    private boolean success;

    public EventResult() {}

    public EventResult(String username, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year, boolean success) {
        this.associatedUsername = username;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
