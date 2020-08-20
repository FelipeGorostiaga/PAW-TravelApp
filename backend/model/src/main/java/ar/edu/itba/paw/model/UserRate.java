package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userRates")
public class UserRate {

    /* package */ UserRate() {
        // Just for Hibernate
    }

    public UserRate(int rate, User ratedUser, User ratedByUser, String comment, LocalDateTime createdOn) {
        this.rate = rate;
        this.ratedByUser = ratedByUser;
        this.ratedUser = ratedUser;
        this.comment = comment;
        this.createdOn = createdOn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRate_id_seq")
    @SequenceGenerator(sequenceName = "userRate_id_seq", name = "userRate_id_seq", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private int rate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedByUser;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedUser;

    @Column(length = 500, nullable = false)
    private String comment;

    private LocalDateTime createdOn;

}
