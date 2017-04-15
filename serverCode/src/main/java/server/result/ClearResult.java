package server.result;

/**
 * Created by Alyx on 2/17/17.
 */

public class ClearResult {
    /** String that contains the Success or Error message */
    private String message;

    /** Creates a ClearResult object */
    public ClearResult(){
        message = "";
    }

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String responseMessage) {
        this.message = responseMessage;
    }
}
