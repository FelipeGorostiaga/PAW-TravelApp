package ar.edu.itba.paw.webapp.form;

import org.springframework.beans.factory.annotation.Required;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TripCommentForm {
    @NotNull
    @Size(min = 1, max = 160)
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
