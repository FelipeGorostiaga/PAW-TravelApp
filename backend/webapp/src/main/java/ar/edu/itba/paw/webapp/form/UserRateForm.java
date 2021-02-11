package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class UserRateForm {

    @NotNull
    private long rateId;

    @NotNull
    private String comment;

    @NotNull
    private int rate;

    public UserRateForm() {

    }

    public long getRateId() {
        return rateId;
    }

    public void setRateId(long rateId) {
        this.rateId = rateId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "UserRateForm{" +
                "rateId=" + rateId +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                '}';
    }
}
