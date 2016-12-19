package net.downloadblog.manic.pushimage.activity;

import java.util.Calendar;

/**
 * Created by ManIc on 9/19/2016.
 */
public class Time {

    public static String calenderTime(Long milis){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(milis);  //here your time in miliseconds
        String date = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + (cl.get(Calendar.MONTH)+1) + ":" + cl.get(Calendar.YEAR);
        String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);

        String t= (cl.get(Calendar.MONTH)+1) +", "+ cl.get(Calendar.DAY_OF_MONTH)+","+ cl.get(Calendar.YEAR)
                +" at "+ cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
        return t;
    }
}
