package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripComment;

import java.time.LocalDateTime;

public class TripCommentDTO {

    private String comment;
    private TripMemberDTO member;
    private LocalDateTime createdOn;

    public TripCommentDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripCommentDTO(TripComment tripComment) {
        comment = tripComment.getComment();
        member = new TripMemberDTO(tripComment.getMember());
        createdOn = tripComment.getCreatedOn();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TripMemberDTO getMember() {
        return member;
    }

    public void setMember(TripMemberDTO member) {
        this.member = member;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
