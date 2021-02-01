package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripStatus;

import java.time.LocalDate;

public class TripDateUtils {

    private TripDateUtils() {
    }

    public static int getTripStatus(Trip trip) {
        if (trip.getStatus() == TripStatus.COMPLETED.ordinal()) return trip.getStatus();
        return LocalDate.now().isBefore(trip.getStartDate()) ?
                TripStatus.DUE.ordinal() : TripStatus.IN_PROGRESS.ordinal();

    }


}
