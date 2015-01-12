package pl.twigit.couplemotivate.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pbrewczynski on 11/19/14.
 */
public class utils {

    public static String representationOfInterval(Calendar calendar1, Calendar calendar2) {
        long differenceInMillis = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        int seconds = (int) ((differenceInMillis / 1000) % 60); // I'm sure the remainder from 60 will be int.
        int minutes = (int) ((differenceInMillis / 1000  / 60) % 60);
        int hours   = (int) ((differenceInMillis / 1000  / 60  / 60 ) % 24);
        int days    = (int) ((differenceInMillis / 1000  / 60  / 60 /   24)); // It should work, when there is less than full 1 day, this last division will effect in 0


        String timeString = String.valueOf(days)    + ":" + String.valueOf(hours)   + ":" +
                            String.valueOf(minutes) + ":" + String.valueOf(seconds);

        return timeString;

    }

    public static  String representReadablyCalendar(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = dateFormat.format(calendar.getTime()).toString();

        return  dateString;
    }
}
