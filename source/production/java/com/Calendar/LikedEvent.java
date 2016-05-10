package com.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by craig on 5/8/16.
 */

@Entity
@Table(name="Liked")
public class LikedEvent implements Serializable {
    private static int eventID;
    private static String username;

    public LikedEvent() {}

    @Id
    @Column(name = "EventID")
    public static int getEventID() {
        return eventID;
    }

    public static void setEventID(int eventID) {
        LikedEvent.eventID = eventID;
    }

    @Column(name = "EventUser")
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LikedEvent.username = username;
    }


}
