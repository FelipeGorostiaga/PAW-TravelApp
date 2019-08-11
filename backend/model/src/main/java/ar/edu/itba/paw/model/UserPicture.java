package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "user_pictures")
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_picture_id_seq")
    @SequenceGenerator(sequenceName = "user_picture_id_seq", name = "user_picture_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "image")
    private byte[] picture;

    //////////////////////

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    ///////////////////////

    public UserPicture(long id, byte[] picture, User user) {
        this(picture, user);
        this.id = id;
    }

    public UserPicture(byte[] picture, User user) {
        this.picture = picture;
        this.user = user;
    }

    /* package */ UserPicture() {
        // Just for Hibernate
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
