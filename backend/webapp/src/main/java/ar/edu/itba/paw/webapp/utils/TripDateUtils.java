package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripStatus;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

public class TripDateUtils {

    private TripDateUtils() {
    }

    public static int getTripStatus(Trip trip) {
        if (trip.getStatus() == TripStatus.COMPLETED.ordinal()) return trip.getStatus();
        return LocalDateTime.now().isBefore(ChronoLocalDateTime.from(trip.getStartDate())) ?
                TripStatus.DUE.ordinal() : TripStatus.IN_PROGRESS.ordinal();

    }


}
