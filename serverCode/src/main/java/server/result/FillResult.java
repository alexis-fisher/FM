package server.result;

/**
 * Created by Alyx on 2/17/17.
 */

public class FillResult {
    /** String that contains the Success or Error message */
    private String message;

    /** Creates a FillResult object */
    public FillResult(){
        message = "";
    }

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String responseMessage) {
        this.message = responseMessage;
    }
}
