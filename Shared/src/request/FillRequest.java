package request;

import result.FillResult;

public class FillRequest {

    private String username;
    private int generations;

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }

    /** a constructor with a specified number of generations to fill
     * @param username
     * @param generations
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }
}
