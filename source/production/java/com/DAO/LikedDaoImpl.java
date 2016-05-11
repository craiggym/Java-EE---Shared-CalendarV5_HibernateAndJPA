package com.DAO;

import com.Calendar.Event;
import com.Calendar.LikedEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LikedDaoImpl implements LikedDao{
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
    public void createLikedTable() {
        String query = "CREATE TABLE Liked(LikeID int, EventID int, EventUser VARCHAR(255));";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    @Override
    public void dropLikedTable() {
        String query = "DROP TABLE Liked;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }


    @Override
    public void insertLiked(LikedEvent likedEvent) {
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
            LikedEvent likedToAdd= new LikedEvent(likedEvent.getLikeID(),likedEvent.getEventID(),likedEvent.getUsername());
            session.save(likedToAdd);
            tx.commit();
            System.out.println("insertion of liked event succeeded.");
        }catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("The insertion of liked event: failed.");
        }finally {
            session.close();
        }
    }

    @Override
    public boolean likedExists(String username) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            String hql = "SELECT COUNT(*) FROM LikedEvent l WHERE l.username=:username";
            Query query = session.createQuery(hql);
            query.setParameter("username",username);

            System.out.println("returning true on likedevents count!");
            return (long) query.list().get(0) > 0;
        } catch (Exception e) {
            System.out.println("There are no liked events");
            if (tx != null) tx.rollback();
        } finally {
            session.close();
        }
        return false;
    }
}