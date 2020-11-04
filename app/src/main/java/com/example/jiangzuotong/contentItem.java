package com.example.jiangzuotong;

import java.io.Serializable;

public class contentItem implements Serializable {
    private int id;
    private String title;
    private String date;
    private String content;

    public contentItem() {
        super();
        title = "";
        date = "";
        content = "";
    }

    public contentItem(String title, String date, String content) {
        super();
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}