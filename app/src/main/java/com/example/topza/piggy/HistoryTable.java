package com.example.topza.piggy;

import android.provider.BaseColumns;

/**
 * Created by topza on 16/6/2559.
 */
public class HistoryTable {

    private int id;
    private String historyMoney;
    private String historyApp;
    private String historyTime;
    private String historyIO;
    private String historyUser;

    public static final String DATABASE_NAME = "history.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "history";

    public HistoryTable() {
    }

    public String getHistoryIO() {
        return historyIO;
    }

    public void setHistoryIO(String historyIO) {
        this.historyIO = historyIO;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }

    public String getHistoryApp() {
        return historyApp;
    }

    public void setHistoryApp(String historyApp) {
        this.historyApp = historyApp;
    }

    public String getHistoryMoney() {
        return historyMoney;
    }

    public void setHistoryMoney(String historyMoney) {
        this.historyMoney = historyMoney;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryUser() {
        return historyUser;
    }

    public void setHistoryUser(String historyUser) {
        this.historyUser = historyUser;
    }

    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String Money = "money";
        public static final String Application = "application";
        public static final String Time = "time";
        public static final String Input_Output = "input_output";
    }
}
