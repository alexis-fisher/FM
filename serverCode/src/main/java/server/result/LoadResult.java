package server.result;

/**
 * Created by Alyx on 2/17/17.
 */

public class LoadResult {
    /** String that contains the Success or Error message */
    private String message;

    /** Creates a LoadResult object */
    public LoadResult(){
        message = "";
    }

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String responseMessage) {
        this.message = responseMessage;
    }
}
