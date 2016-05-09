package com.DAO;

import com.Calendar.Event;
import com.Calendar.LikedEvent;
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


    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createLikedTable() {
        String query = "CREATE TABLE Liked(EventID int, EventUser VARCHAR(255));";
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
        String query = "INSERT INTO Liked (EventID, EventUser) VALUES (?,?);";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {likedEvent.getEventID(), likedEvent.getUsername()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
    }

}