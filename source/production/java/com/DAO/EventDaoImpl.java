package com.DAO;

import com.Calendar.Event;
import com.Calendar.LikedEvent;
import com.Calendar.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EventDaoImpl implements EventDao{
    // class variables //
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private static boolean debug = true;
    private static SessionFactory factory;

    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createEventTable() {
        String query = "CREATE TABLE Event(EventID int, EventName VARCHAR(255), EventDate Date, EventDesc VARCHAR(255), EventUser VARCHAR(255), EventCreator VARCHAR(255));";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    @Override
    public void dropEventTable() {
        String query = "DROP TABLE Event;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /***************************************************************
     * Title: insertEvent
     * Description: Add an event to the database
     * @param event An Event object
     ***************************************************************/
    @Override
    public void insertEvent(Event event) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Event eventToAdd = new Event(event.getId(), event.getEventName(),event.getEventDate(), event.getEventDescription(),event.getUsername(),event.getEventAuthor());
            session.save(eventToAdd);
            tx.commit();
            System.out.println("insertion of event: " +event.getEventName() + " succeeded.");
        }catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("The insertion of event: " +event.getEventName() + " failed.");
        }finally {
            session.close();
        }
    }

    @Override
    public boolean eventsExists(String username) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            String hql = "SELECT COUNT(*) FROM Event e WHERE e.username=:username";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            System.out.println("returning True on eventsExist(username)");
            return true;
        }catch (Exception e) {
            System.out.println("There are no events for user: "+username+" or problem querying.");
            if (tx!=null) tx.rollback();
        }finally {
            session.close();
        }
        System.out.println("oh no returning false");
        return false;
    }

    @Override
    public boolean hasEvent(String eventname, String username, String creator) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            String hql = "SELECT e FROM Event e WHERE e.eventname=:eventname AND e.username=:username AND e.creator=:creator";
            Query query = session.createQuery(hql);
            query.setParameter("eventname", eventname);
            query.setParameter("username", username);
            query.setParameter("creator", creator);
            Event eventResult = (Event) query.list().get(0);
            return true;
        }catch (Exception e) {
            System.out.println("user does not have the event to be added");
            if (tx!=null) tx.rollback();
        }finally {
            session.close();
        }
        return false;
    }

    @Override
    public List<Event> selectAllEvent(String username) {
        try{
            LikedDaoImpl likedDao = new LikedDaoImpl();
            String query;
            query = "SELECT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event AS e RIGHT JOIN Liked as l ON e.EventID=l.EventID WHERE l.EventUser=\'"+username+"\' ORDER BY EventDate ASC";

            jdbcTemplate = new JdbcTemplate(dataSource);
            List<Event> events = jdbcTemplate.query(query, new EventMapper());
            return events;
        }catch(Exception e){
            System.out.println("error in selectAllEvents(username) method.");return null;}

    }


    @Override
    public int countEvents() {
        try {
            String query = "SELECT MAX(EventID) FROM Event";
            jdbcTemplate = new JdbcTemplate(dataSource);
            int res = (int) jdbcTemplate.queryForObject(query, int.class);

            return res;
        }
        catch(Exception e){
            if (debug) System.out.println("error querying for count");
            return 0;
        }
    }

    @Override
    public List<Event> selectAllEvents() {
        try {
            String query = "SELECT DISTINCT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser=EventCreator ORDER BY EventDate ASC";
            jdbcTemplate = new JdbcTemplate(dataSource);
            List<Event> events = jdbcTemplate.query(query, new EventMapper());
            return events;
        }catch(Exception e){System.out.println("error in selectAllEvents() method.");return null;}
    }

    @Override
    public Event getEventById(int id) {
        try {
            String query = "select EventID, EventName, EventDate, EventDesc, EventUser, EventCreator from Event where EventID=" + id + " limit 1";
            jdbcTemplate = new JdbcTemplate(dataSource);
            List<Event> events = jdbcTemplate.query(query, new EventMapper());
            if (events != null && events.size() > 0) {
                return events.get(0);
            }
            return null;
        }catch(Exception e){System.out.println("error in getEventById(id) method.");return null;}
    }
}