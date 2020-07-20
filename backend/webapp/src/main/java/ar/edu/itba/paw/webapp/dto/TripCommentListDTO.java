package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripComment;

import java.util.ArrayList;
import java.util.List;

public class TripCommentListDTO {

    List<TripCommentDTO> comments;

    public TripCommentListDTO() {

    }

    public TripCommentListDTO(List<TripComment> list) {
        this.comments = new ArrayList<>();
        list.forEach(t -> this.comments.add(new TripCommentDTO(t)));
    }

    public List<TripCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TripCommentDTO> comments) {
        this.comments = comments;
    }
}
