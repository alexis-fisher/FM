package server.request;

/**
 * Created by Alyx on 3/18/17.
 */

public class FillRequest {


    private int generations;
    private String userName;

    public FillRequest(){
        this.userName = "";
        this.generations = 0;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
