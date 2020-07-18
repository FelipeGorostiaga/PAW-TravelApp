package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripListDTO;
import ar.edu.itba.paw.webapp.dto.TripListListDTO;

import java.util.List;

public class ListChopper {

    public static TripListListDTO chopped(List<Trip> list, final int L) {
        TripListListDTO ret = new TripListListDTO();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            TripListDTO tripListDTO = new TripListDTO(list.subList(i, Math.min(N, i + L)));
            ret.addList(tripListDTO);
        }
        return ret;
    }
}
