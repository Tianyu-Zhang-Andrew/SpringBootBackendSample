package com.mikason.PropView.dataaccess.commercialEntity;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Getter
public class Duration {
    private int year;
    private int mouth;
    private int day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    public Duration(int year, int mouth, int day, int startHour,int startMinute, int endHour, int endMinute){
        this.year = year;
        this.mouth = mouth;
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public Duration(){}

    public String toString(){
        Gson gson = new Gson();
        String strObj = gson.toJson(this);
        return strObj;
    }

    private String format(int num){
        if(num < 10){
            return "0" + String.valueOf(num);
        }else{
            return String.valueOf(num);
        }
    }

    public Date getStartTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String startDateStr = String.valueOf(this.year) + "-" + format(this.mouth) +"-"+ format(this.day)
                + " " + format(this.startHour) + ":" + format(this.startMinute);
        try {
            return sdf.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Date getEndTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String startDateStr = String.valueOf(this.year) + "-" + format(this.mouth) + "-" + format(this.day)
                + " " + format(this.endHour) + ":" + format(this.endMinute);
        try {
            return sdf.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
