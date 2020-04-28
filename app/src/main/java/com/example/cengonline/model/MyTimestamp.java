package com.example.cengonline.model;



import android.util.Log;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;


public class MyTimestamp extends Timestamp implements Serializable {

    public MyTimestamp(){
        super(new Date().getTime());
    }

    public MyTimestamp(Date date){
        super(date.getTime());
    }

    @Override
    public String toString(){

        try{
            String str = super.toString();
            int monthInt = Integer.parseInt(str.substring(5, 7));
            int dayInt = Integer.parseInt(str.substring(8, 10));
            String monthStr = "";
            switch(monthInt){
                case 1: monthStr = "Jan"; break;
                case 2: monthStr = "Feb"; break;
                case 3: monthStr = "Mar"; break;
                case 4: monthStr = "Apr"; break;
                case 5: monthStr = "May"; break;
                case 6: monthStr = "Jun"; break;
                case 7: monthStr = "Jul"; break;
                case 8: monthStr = "Aug"; break;
                case 9: monthStr = "Sep"; break;
                case 10: monthStr = "Oct"; break;
                case 11: monthStr = "Nov"; break;
                case 12: monthStr = "Dec"; break;
            }
            int yearInt = Integer.parseInt(str.substring(0, 4));
            int nowYear = Integer.parseInt(new Timestamp(new Date().getTime()).toString().substring(0, 4));
            if(nowYear != yearInt){
                return monthStr + " " + str.substring(8, 10) + ", " + yearInt;
            }
            else{
                int nowDay = Integer.parseInt(new Timestamp(new Date().getTime()).toString().substring(8, 10));
                if(nowDay != dayInt){
                    return monthStr + " " + dayInt;
                }
                else{
                    return str.substring(11, 16);
                }
            }
        }
        catch(Exception ex){
            return super.toString();
        }
    }
}
