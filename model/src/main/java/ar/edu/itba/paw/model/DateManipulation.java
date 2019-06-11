package ar.edu.itba.paw.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateManipulation {

    /*Utility class, enforce noninstantiability*/
    private DateManipulation() {

    }

    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar stringToCalendar(String string) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(string);
            cal.setTime(date);
        }
        catch(Exception e) {
        }
        return cal;
    }

}
