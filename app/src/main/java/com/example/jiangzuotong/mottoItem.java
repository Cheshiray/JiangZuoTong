package com.example.jiangzuotong;

public class mottoItem {
    private int id;
    private int date_1;
    private int date_2;
    private String content;

    public mottoItem() {
        super();
        date_1 = -1;
        date_2 = 1;
        content = "";
    }

    public mottoItem(int date_1, int date_2, String content) {
        super();
        this.date_1 = date_1;
        this.date_2 = date_2;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate_1() {
        return date_1;
    }

    public void setDate_1(int date_1) {
        this.date_1 = date_1;
    }

    public int getDate_2() {
        return date_2;
    }

    public void setDate_2(int date_2) {
        this.date_2 = date_2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}