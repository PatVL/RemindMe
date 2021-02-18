package com.example.reminderappremindme;

public class Datas {
    int id;
    private String title;
    private String date;
    private String time;

    Datas(int id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    int findId() {
        return id;
    }

    String findTitle() {
        return title;
    }

    String findDate() {
        return date;
    }

    String findTime() {
        return time;
    }

}
