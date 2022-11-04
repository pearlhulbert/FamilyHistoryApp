package result;

import model.Person;

public class FamilyResult {

    private Person[] data;
    private String message;
    private boolean success;

    public FamilyResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public FamilyResult() {}


    public FamilyResult(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public Person[] getFamily() {
        return data;
    }

    public void setFamily(Person[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }
}
