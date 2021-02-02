package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TripHibernateDao implements TripDao {

    private static final int MAX_ROWS = 6;

    @PersistenceContext
    EntityManager em;

    @Override
    public Trip create(User creator, Place startPlace, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate) {
        Trip trip = new Trip(startPlace, name, description, startDate, endDate, isPrivate);
        em.persist(trip);
        TripMember creatorMember = new TripMember(trip, creator, TripMemberRole.CREATOR);
        em.persist(creatorMember);
        return trip;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.ofNullable(em.find(Trip.class, id));
    }

    @Override
    public List<Trip> findByName(String name) {
        final TypedQuery<Trip> query = em.createQuery("From Trip as t where lower(t.name) like lower(:name)", Trip.class);
        query.setParameter("name", "%" + name + "%");
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> getAllTripsPerPage(int pageNum) {
        final TypedQuery<Trip> query = em.createQuery("From Trip", Trip.class);
        query.setFirstResult((pageNum - 1) * MAX_ROWS);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public TripPendingConfirmation createPendingConfirmation(Trip trip, User user, String token) {
        TripPendingConfirmation pendingConfirmation = new TripPendingConfirmation(trip, user, token);
        em.persist(pendingConfirmation);
        return pendingConfirmation;
    }

    @Override
    public List<TripPendingConfirmation> getTripJoinRequests(long tripId) {
        final TypedQuery<TripPendingConfirmation> query = em.createQuery("FROM TripPendingConfirmation as tp WHERE tp.trip.id = :tripId", TripPendingConfirmation.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }

    @Override
    public boolean editJoinRequest(Trip trip, User u, String token, boolean accepted) {
        Query query = em.createQuery("UPDATE TripPendingConfirmation set accepted = :accepted, edited = true where token = :token");
        query.setParameter("accepted", accepted);
        query.setParameter("token", token);
        return query.executeUpdate() != 0;
    }

    @Override
    public Optional<TripPendingConfirmation> findJoinRequestByToken(String token) {
        final TypedQuery<TripPendingConfirmation> query = em.createQuery("From TripPendingConfirmation as tp where tp.token = :token", TripPendingConfirmation.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public TripInvitation createTripInvitation(Trip trip, User invitedUser, User admin, String token) {
        TripInvitation invitation = new TripInvitation(trip, invitedUser, admin, token);
        em.persist(invitation);
        return invitation;
    }

    @Override
    public Optional<TripPendingConfirmation> findTripConfirmationByUser(Trip trip, User user) {
        final TypedQuery<TripPendingConfirmation> query = em.createQuery("From TripPendingConfirmation as tpc where tpc.trip.id = :tripId  and tpc.requestingUser.id = :userId", TripPendingConfirmation.class);
        query.setParameter("tripId", trip.getId());
        query.setParameter("userId", user.getId());
        List<TripPendingConfirmation> resultList = query.getResultList();
        return resultList.stream().findAny();
    }

    @Override
    public Optional<TripInvitation> findTripInvitationByToken(String token) {
        final TypedQuery<TripInvitation> query = em.createQuery("From TripInvitation as ti where ti.token = :token", TripInvitation.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteTripInvitation(String token, Trip trip) {
        Query query = em.createQuery("DELETE TripInvitation as ti where ti.token = :token AND ti.trip.id = :tripId");
        query.setParameter("token", token);
        query.setParameter("tripId", trip.getId());
        query.executeUpdate();
    }

    @Override
    public Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user) {
        final TypedQuery<TripInvitation> query = em.createQuery("From TripInvitation as ti where ti.trip.id = :tripId AND ti.invitee.id = :userId", TripInvitation.class);
        query.setParameter("tripId", trip.getId());
        query.setParameter("userId", user.getId());
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void updateTripData(String tripName, String description, long tripId) {
        Query query = em.createQuery("UPDATE Trip set name = :tripName, description = :description where id = :tripId");
        query.setParameter("tripName", tripName);
        query.setParameter("description", description);
        query.setParameter("tripId", tripId);
        query.executeUpdate();
    }


    @Override
    public TripMember createTripMember(Trip trip, User user, TripMemberRole role) {
        TripMember member = new TripMember(trip, user, role);
        em.persist(member);
        return member;
    }

    @Override
    public void updateRoleToAdmin(long tripId, long userId) {
        Query query = em.createQuery("UPDATE TripMember set role = :adminRole where trip.id = :tripId and user.id = :userId");
        query.setParameter("adminRole", TripMemberRole.MEMBER.name());
        query.setParameter("tripId", tripId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }


    @Override
    public List<Trip> getAllTrips() {
        final TypedQuery<Trip> query = em.createQuery("FROM Trip", Trip.class);
        return query.getResultList().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Trip> findUserCreatedTrips(long userId) {
        final TypedQuery<Trip> query = em.createQuery("FROM Trip as t where t.adminId = :userId ", Trip.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void deleteTrip(long tripId) {
        Query tripDelete = em.createQuery("delete Trip as t where t.id = :id");
        tripDelete.setParameter("id", tripId);
        tripDelete.executeUpdate();
    }

    public int countAllTrips() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Trip", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public List<Trip> findByCategory(String category) {
        final TypedQuery<Trip> query = em.createQuery("select t From Trip as t, Activity as a" +
                " where a.trip.id = t.id and a.category like :category", Trip.class);
        query.setParameter("category", category);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> findByPlace(String placeName) {
        final TypedQuery<Trip> query = em.createQuery("select t From Trip as t, Place as p" +
                " where t.startPlace.id = p.id and lower(p.address) like lower(:placeName)", Trip.class);
        query.setParameter("placeName", "%" + placeName + "%");
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> findWithFilters(Map<String, Object> filterMap) {
        final TypedQuery<Trip> query = em.createQuery("select distinct t From Trip as t, Place as p " +
                        filtersQuery(filterMap), Trip.class);
        setQueryParameters(query, filterMap);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    private void setQueryParameters(TypedQuery<Trip> query, Map<String, Object> filterMap) {
        for(String filter : filterMap.keySet()) {
            if(filter.equals("placeName")) {
                query.setParameter(filter, "%" +  filterMap.get(filter) + "%");
            }
            else if (filter.equals("category")){
                query.setParameter(filter, filterMap.get(filter));
            }
            else {
                query.setParameter(filter, filterMap.get(filter)) ;
            }
        }
    }


    private String filtersQuery(Map<String, Object> filterMap) {
        int count = 0;
        StringBuilder buffer = new StringBuilder();
        if(filterMap.containsKey("category") || filterMap.containsKey("placeName")) {
            buffer.append(", Activity as a ");
        }
        for(String filter : filterMap.keySet()) {
            switch(filter) {
                case "placeName":
                    if(count == 0) {
                        buffer.append(" where ");
                    }
                    else {
                        buffer.append(" and  ");
                    }
                    buffer.append("((t.startPlaceId = p.id and (lower(p.address) like lower(:placeName) or lower(p.name) like lower(:placeName)) )");
                    buffer.append("or (a.trip.id = t.id and ( lower(a.place.name) like lower(:placeName) or lower(a.place.address) like lower(:placeName))))");
                    count++;
                    break;

                case "startDate":
                    if(count == 0) {
                        buffer.append(" where ");
                    }
                    else {
                        buffer.append(" and ");
                    }
                    buffer.append("(t.startDate = :startDate)");
                    count++;
                    break;
                case "category":
                    if(count == 0) {
                        buffer.append(" where ");
                    }
                    else {
                        buffer.append(" and ");
                    }
                    buffer.append("(a.trip.id = t.id and a.category like :category)");
                    count++;
                    break;
                case "endDate":
                    if(count == 0) {
                        buffer.append(" where ");
                    }
                    else {
                        buffer.append(" and ");
                    }
                    buffer.append("(t.endDate = :endDate)");
                    count++;
                    break;
            }
        }
        return buffer.toString();
    }

    @Override
    public List<TripComment> getTripComments(long tripId) {
        final TypedQuery<TripComment> query = em.createQuery("From TripComment as tc where tc.member.trip.id = :tripId", TripComment.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }

}
