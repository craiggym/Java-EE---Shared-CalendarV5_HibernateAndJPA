package com.DAO;

import com.Calendar.Event;
import com.Calendar.LikedEvent;

import java.util.List;

public interface LikedDao {
    void insertLiked(LikedEvent likedEvent);
    void createLikedTable();
    void dropLikedTable();
    boolean likedExists();
}
