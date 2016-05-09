package com.Calendar;

/**
 * Created by craig on 5/8/16.
 */
public class LikedEvent {
    private static int eventID;
    private static String username;

    public LikedEvent() {
    }

    public static int getEventID() {
        return eventID;
    }

    public static void setEventID(int eventID) {
        LikedEvent.eventID = eventID;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LikedEvent.username = username;
    }


}
