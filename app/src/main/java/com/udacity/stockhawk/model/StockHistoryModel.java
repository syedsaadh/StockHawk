package com.udacity.stockhawk.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Xeda on 03-02-2017.
 */

public class StockHistoryModel {
    private String timeInMillis;
    private String closeValue;
    public StockHistoryModel (String timeInMillis, String closeValue){
        this.timeInMillis = timeInMillis;
        this.closeValue = closeValue;
    }

    public Float getCloseValue() {
        return Float.valueOf(closeValue);
    }

    public long getTimeInMillis() {
        return Long.valueOf(timeInMillis);
    }
    public String getTimeString(){
        return getDateDay(getTimeInMillis());
    }
    private String getDateDay(long timeInMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return formatter.format(calendar.getTime());
    }

    public void setCloseValue(String closeValue) {
        this.closeValue = closeValue;
    }

    public void setTimeInMillis(String timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
