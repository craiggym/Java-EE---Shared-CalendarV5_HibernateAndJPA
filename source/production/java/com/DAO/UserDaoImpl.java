package com.DAO;

import com.Calendar.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;


import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;


public class UserDaoImpl implements UserDao{
    // class variables //
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    boolean debug = true;
    private static SessionFactory factory;


    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /***************************************************************
     * Title: createUserTable
     * Description: Create the User table
     ***************************************************************/
    @Override
    public void createUserTable() {
        String query = "CREATE TABLE User(userID int, username VARCHAR(255), e_mail VARCHAR(255), password VARCHAR(255), " +
                "first_name VARCHAR(255), last_name VARCHAR(255));";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /*****************************************************************************************
     * Title: dropUserTable
     * Description: Drops table on a new instance of the web application
     ****************************************************************************************/
    @Override
    public void dropUserTable() {
        String query = "DROP TABLE User;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /*****************************************************************************
     * Added for last assignment below
     */
    @Override
    public boolean userExists(String username) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = selectUser(username);
            session.close();
            if(user == null) return false;
        }catch(Exception e){
            tx.rollback();
            session.close();
            System.out.println("User not in database");
        }
        return true;
    }

    @Override
    public int insertUser(User user) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        int employeeID=-1;
        try{
            tx = session.beginTransaction();
            User userToAdd = new User(user.getUserID(), user.getUsername(), user.getE_mail(), user.getPassword(),user.getFirst_name(), user.getLast_name());
            session.save(userToAdd);
            tx.commit();
            System.out.println("insertion of user: " +user.getUsername() + " succeeded.");
        }catch (Exception e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            System.out.println("The insertion of user: " +user.getUsername() + " failed.");
        }finally {
            session.close();
        }
        return employeeID;
    }

    @Override
    public User selectUser(String username) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        System.out.println("Entered selectUser with user " + username);
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            String hql = "SELECT u FROM User u WHERE u.username= :username";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            User u = (User) query.list().get(0);
            return u;
        }catch (Exception e) {
            System.out.println("There was a problem with the selectUser procedure");
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;

    }

    @Override
    public int selectUserID(String username) {
        try{
            User user = selectUser(username);
            return user.getUserID();
        }catch(Exception e){}
        return -1;

    }

    @Override
    public boolean isAuthCorrect(String username, String password) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;
        try{
            String hql = "SELECT u FROM User u WHERE u.username= :username AND u.password=:password";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            query.setParameter("password", password);
            User u = (User) query.list().get(0);
            if (u != null){
                System.out.println("User is authorized");
                return true;
            }
        }catch(Exception e){}
        System.out.println("returning false on AUTH");
        return false;

    }


}
