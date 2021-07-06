package ar.edu.itba.paw.webapp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateManipulation {

    /* Utility class, enforce non-instantiability */
    private DateManipulation() {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static LocalDate stringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
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
