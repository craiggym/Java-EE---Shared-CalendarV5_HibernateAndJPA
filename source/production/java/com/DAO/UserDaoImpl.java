package com.DAO;

import com.Calendar.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;


import javax.sql.DataSource;
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

    /*****************************************************************************************
     * Title: insertUser
     * Description: Add a user to the database
     * @param user User object
     ****************************************************************************************/
 /*   @Override
    public void insertUser(User user) {
        String query = "insert into User (userID, username, e_mail, password, first_name, last_name) values (?,?,?,?,?,?)";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {user.getUserID(), user.getUsername(),user.getE_mail(), user.getPassword(), user.getFirst_name(), user.getLast_name()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
        if(debug) System.out.printf("User: %s added with password: %s", user.getUsername(), user.getPassword());
    }

    // Boolean checking //
    /*****************************************************************************************
     * userExists
     * @param username
     * @return true if the user exists in the database
     ****************************************************************************************/
  /*  @Override
    public boolean userExists(String username) {
        try {
            String query = "SELECT username FROM User WHERE username=?";
            Object[] input = new Object[]{username};
            jdbcTemplate = new JdbcTemplate(dataSource);
            String uname = (String) jdbcTemplate.queryForObject(query, input, String.class);

            if(debug) {
                System.out.println("result of query: " + uname);
                System.out.println("User exists");
            }
            return true;
        }
        catch(Exception e){
            if (debug) System.out.println("User does not exist");
            return false;
        }
    }
    // count update//
    /*****************************************************************************************
     * countUser
     * @return count of users in user table
     ****************************************************************************************/
 /*   @Override
    public int countUsers() {
        try {
            String query = "SELECT COUNT(*) FROM User";
            jdbcTemplate = new JdbcTemplate(dataSource);
            int res = (int) jdbcTemplate.queryForObject(query, int.class);

            return res;
        }
        catch(Exception e){
            if (debug) System.out.println("error querying for count");
            return 0;
        }
    }

    /*****************************************************************************************
     * isAuthCorrect
     * Authenticates using two strings passed in parameters
     * @param username
     * @param password
     * @return true if authenticated correctly
     *****************************************************************************************/
/*    @Override
    public boolean isAuthCorrect(String username, String password) {
        try {
            String query = "SELECT username FROM User WHERE username=?" + " AND password=?";
            Object[] input = new Object[]{username,password};
            jdbcTemplate = new JdbcTemplate(dataSource);
            String q_result = (String) jdbcTemplate.queryForObject(query, input, String.class);

            if(debug)System.out.println("Authentication for " + username + " correct!(result="+q_result +")");
            return true;
        }
        catch(Exception e){
            if (debug) System.out.println("Authentication for " + username + " incorrect");
            return false;
        }
    }

    // Select statements //
    /*****************************************************************************************
     * selectFirstName
     * Selects the user's first name
     * @param username
     * @return
     ****************************************************************************************/
 /*   @Override
    public String selectFirstName(String username) {
        String query = "SELECT first_name FROM User WHERE username=?";
        Object[] input = new Object[] {username};
        jdbcTemplate = new JdbcTemplate(dataSource);
        String fname = (String)jdbcTemplate.queryForObject(query, input, String.class);
        return fname;
    }

    /**
     * selectUserID
     * Grabs the unique identifier from the user table given the username
     * @param username
     * @return
     */
/*    @Override
    public int selectUserID(String username) {
        String query = "SELECT UserID FROM User WHERE username=?";
        Object[] input = new Object[] {username};
        jdbcTemplate = new JdbcTemplate(dataSource);
        int uid = (int) jdbcTemplate.queryForObject(query, input, Integer.class);

        return uid;
    }
*/
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
            String uname = user.getUsername(); // Test for null. Goes into catch if null
            tx.commit();
            session.close();
            return true;
        }catch(Exception e){
            tx.rollback();
            session.close();
            System.out.println("User not in database");
        }
        return false;
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
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User) session.createQuery("FROM User WHERE Username = \'"+username+"\'").list();
            tx.commit();
            return user;
        }
        catch(Exception e){
            tx.rollback();
            System.out.println("CATCH in selecting user: " + username);
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
        }catch(NullPointerException n){}
        return -1;

    }

    @Override
    public boolean isAuthCorrect(String username, String password) {
        try{
            User user = selectUser(username);
            return (password == user.getPassword());
        }catch(NullPointerException n){}
        return false;

    }

    public int countUsers() {
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
            int theCount = session.createQuery("SELECT COUNT(*) FROM User").getFirstResult();
            tx.commit();
            return theCount;
        }catch(Exception e){
            tx.rollback();
            System.out.println("CATCH on counting users in database");
        }finally {
            session.close();
        }
        return -1;
    }
}
