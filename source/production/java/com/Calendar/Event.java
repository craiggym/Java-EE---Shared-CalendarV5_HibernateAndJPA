package com.Calendar;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Event implements Serializable {
    private Integer id;
    private String eventName;
    private Date eventDate;
    private String eventDescription;
    private String username;
    private String eventAuthor;
    private String month,day,year;

    public Event(){};
    public Event(Integer id, String name, Date date, String desc, String uname, String author) {
        this.id = id;
        this.eventName = name;
        this.eventDate = date;
        this.eventDescription = desc;
        this.username = uname;
        this.eventAuthor = author;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EventId")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "EventName")
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "EventDate")
    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Column(name = "EventDesc")
    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    @Column(name = "EventUser")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "EventCreator")
    public String getEventAuthor() {
        return eventAuthor;
    }

    public void setEventAuthor(String eventAuthor) {
        this.eventAuthor = eventAuthor;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) { this.year = year;  }


}
