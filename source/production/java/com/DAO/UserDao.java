package com.DAO;

import com.Calendar.User;

public interface UserDao  extends JpaDao<User, Long>{
/*
    int countUsers();




*/
    void createUserTable();
    void dropUserTable();

    boolean userExists(String username);
    void insertUser(User user);
    int selectUserID(String username);
    User selectUser(String username);
    boolean isAuthCorrect(String username, String password);
    int countUsers();
}
