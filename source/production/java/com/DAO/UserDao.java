package com.DAO;

import com.Calendar.User;

public interface UserDao {
    void createUserTable();
    void dropUserTable();
    void insertUser(User user);
    int countUsers();
    int selectUserID(String username);
    String selectFirstName(String username);
    boolean userExists(String username);
    boolean isAuthCorrect(String username, String password);
}
