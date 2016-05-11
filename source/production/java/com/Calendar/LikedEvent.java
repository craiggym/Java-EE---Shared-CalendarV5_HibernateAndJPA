package com.Calendar;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by craig on 5/8/16.
 */


public class LikedEvent implements Serializable {
    private int likeID;
    private int eventID;
    private String username;

    public LikedEvent() {}

    public LikedEvent(int likeID, int eventID, String username) {
        this.likeID = likeID;
        this.eventID = eventID;
        this.username = username;
    }

    public int getLikeID() {
        return likeID;
    }

    public void setLikeID(int likeID) {
        this.likeID = likeID;
    }



    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
