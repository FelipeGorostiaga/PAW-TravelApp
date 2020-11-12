package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class TripCommentForm {

    public TripCommentForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
