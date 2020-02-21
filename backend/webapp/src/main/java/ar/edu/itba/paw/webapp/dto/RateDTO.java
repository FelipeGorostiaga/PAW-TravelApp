package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripRate;

import java.time.LocalDateTime;

public class RateDTO {

    private int rate;
    private LocalDateTime ratedOn;

    public RateDTO() {

    }

    public RateDTO(TripRate tripRate) {
        this.rate = tripRate.getRate();
        this.ratedOn = tripRate.getCreatedOn();
    }

}
