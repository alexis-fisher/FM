package server.result;

import server.model.Event;

/**
 * Created by Alyx on 2/17/17.
 */

public class EventResult {
    public Event[] data;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event event;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    public EventResult(){
        event = null;
    }


}
