package ar.edu.itba.paw.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateManipulation {

    /* Utility class, enforce non-instantiability */
    private DateManipulation() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    public static LocalDate stringToLocalDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            System.out.println("String converted to date correctly");
            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Date manipulation exception");
            return null;
        }
    }

    public static String changeDateFormat(LocalDate date) {
        return date.format(formatter);
    }

    public static String changeDateTimeFormat(LocalDateTime date) {
        return date.format(timeFormatter);
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
