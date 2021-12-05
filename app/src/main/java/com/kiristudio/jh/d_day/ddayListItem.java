package com.kiristudio.jh.d_day;

/**
 * Created by lee on 2015-07-20.
 */
public class ddayListItem {

    String title;
    long dday;
    String date;

    public void setDate(String date) {
        this.date = date;
    }

    public void setDday(long dday) {
        this.dday = dday;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public long getDday() {
        return dday;
    }

    public String getTitle() {
        return title;
    }
}
