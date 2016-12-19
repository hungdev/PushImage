package net.downloadblog.manic.pushimage.activity;

import java.util.Date;

/**
 * Created by ManIc on 9/16/2016.
 */
public class GetTime {
    private void timeSpan(long timeStart, long timeEnd){
        Date start = new Date(timeStart); // JANUARY_1_2007
        Date end = new Date(timeEnd); // APRIL_1_2007


        long diffInSeconds = (end.getTime() - start.getTime()) / 1000;

        long diff[] = new long[] { 0, 0, 0, 0 };
    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));
//"%d day%s, %d hour%s, %d minute%s, %d second%s ago",
        String.format(
                "%d day%s, %d hour%s, %d minute%s, %d second%s ago",
                diff[0],
                diff[0] > 1 ? "s" : "",
                diff[1],
                diff[1] > 1 ? "s" : "",
                diff[2],
                diff[2] > 1 ? "s" : "",
                diff[3],
                diff[3] > 1 ? "s" : "");
    }
}
