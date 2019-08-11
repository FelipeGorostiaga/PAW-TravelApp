package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "activities")
public class Activity implements Comparable<Activity> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_id_seq")
    @SequenceGenerator(sequenceName = "activity_id_seq", name = "activity_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 40, nullable = false)
    private String category;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    ///////////////

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Place place;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    ////////////////

    public Activity(long id, String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate) {
        this(name, category, place, trip, startDate, endDate);
        this.id = id;
    }

    public Activity(String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.category = category;
        this.place = place;
        this.trip = trip;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    /* package */ Activity() {
        // Just for Hibernate
    }


    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id &&
                Objects.equals(name, activity.name) &&
                Objects.equals(category, activity.category) &&
                Objects.equals(startDate, activity.startDate) &&
                Objects.equals(endDate, activity.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, startDate, endDate, place, trip);
    }

    @Override
    public int compareTo(Activity o) {
        return (this.startDate.isBefore(o.startDate)) ? -1 : 1;
    }
}
