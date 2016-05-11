package com.DAO;

import com.Calendar.Event;
import java.util.List;

public interface EventDao {
    void insertEvent(Event event);
    void createEventTable();
    void dropEventTable();
    boolean eventsExists(String username);
    boolean hasEvent(String eventname, String username, String creator);
    int countEvents();
    List<Event> selectAllEvent(String username);
    List<Event> selectAllEvents();
    Event getEventById(int id);
}
