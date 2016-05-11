package com.DAO;

import com.Calendar.User;

public interface UserDao {
/*
    int countUsers();




*/
    void createUserTable();
    void dropUserTable();

    boolean userExists(String username);
    int insertUser(User user);
    int selectUserID(String username);
    User selectUser(String username);
    boolean isAuthCorrect(String username, String password);
}
