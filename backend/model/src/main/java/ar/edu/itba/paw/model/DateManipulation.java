package ar.edu.itba.paw.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateManipulation {

    /* Utility class, enforce non-instantiability */
    private DateManipulation() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static LocalDate stringToLocalDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String changeDateFormat(LocalDate date) {
        return date.format(formatter);
    }

    public static boolean validate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
