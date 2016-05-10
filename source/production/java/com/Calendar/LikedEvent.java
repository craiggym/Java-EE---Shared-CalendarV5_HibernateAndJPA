package com.Calendar;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by craig on 5/8/16.
 */

@Entity
@Table(name="Liked")
public class LikedEvent implements Serializable {
    private int likeID;
    private int eventID;
    private String username;

    public LikedEvent() {}

    @Id
    @GeneratedValue
    @Column(name = "LikeID", nullable = false)
    public int getLikeID() {
        return likeID;
    }

    public void setLikeID(int likeID) {
        this.likeID = likeID;
    }


    @Column(name = "EventID")
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    @Column(name = "EventUser")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
