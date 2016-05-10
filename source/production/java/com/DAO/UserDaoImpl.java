package com.DAO;

import com.Calendar.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;

public class UserDaoImpl extends JpaDaoImpl<User, Long> implements UserDao{
    // class variables //
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    boolean debug = true;


    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public UserDaoImpl() {
        super(User.class);
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
        try {
            Query q = getEntityManager()
                .createQuery("SELECT COUNT(*) FROM " + getPersistentClass().getSimpleName() + //If table name changes, this still works
                        " u WHERE u.username = :username")
                .setParameter("username", username);

            int count = (int) q.getSingleResult();
            return count >= 1;
        }
        catch(Exception e){
            System.out.println("Bad things happened in the userExist query for user: "+username);
        }
        return false;

    }

    @Override
    public void insertUser(User user) {
        try{
            userDao.save(user);
        }catch(Exception e){
            System.out.println("There was an issue with inserting the user " + user.getUsername());
        }
    }

    @Override
    public User selectUser(String username) {
        Query q = getEntityManager()
                .createQuery("SELECT u from " + getPersistentClass().getSimpleName() +
                        " u WHERE u.username = :username")
                .setParameter("username", username);

        User user = null;
        try{
            user = (User) q.getSingleResult();
        }
        catch(Exception e){
            System.out.println("User " + username + " not found!");
        }
        return user;
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
        Query q = getEntityManager()
                .createQuery("SELECT COUNT(*) from " + getPersistentClass().getSimpleName() +
                        " u");
        try{
            int result = (int)q.getSingleResult();
            return result;
        }
        catch(NullPointerException n){}
        return -1;

    }
}
