package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListChopper {

    public static List<List<TripDTO>> chopped(List<Trip> list, final int L) {
        List<List<TripDTO>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            List<Trip> auxList = new ArrayList<>(list.subList(i, Math.min(N, i + L)));
            parts.add(auxList.stream().map(TripDTO::new).collect(Collectors.toList()));
        }
        return parts;
    }
}
