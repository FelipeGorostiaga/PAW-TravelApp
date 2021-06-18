package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TripHibernateDao implements TripDao {

    /* *
    *
    *   Hibernate Pagination as stated here:
    *   https://thorben-janssen.com/pagination-jpa-hibernate/#Pitfall_2_Pagination_With_JOIN_FETCH_and_EntityGraphs_Clauses
    *
    * */

    private static final int MAX_ROWS = 6;
    private static final int MAX_SEARCH_RESULTS = 3;
    private static final int ADV_MAX_SEARCH_RESULTS = 2;

    @PersistenceContext
    private EntityManager em;

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
    public List<Trip> findByName(String name, int page) {

        // Get primary keys with LIMIT and OFFSET
        final TypedQuery<Long> idQuery = em.createQuery("SELECT t.id FROM Trip t where lower(t.name) like lower(:name) AND t.isPrivate = false order by t.startDate", Long.class);
        List<Long> tripIds = idQuery
                .setParameter("name", "%" + name + "%")
                .setFirstResult((page - 1) * MAX_SEARCH_RESULTS)
                .setMaxResults(MAX_SEARCH_RESULTS)
                .getResultList();

        if (tripIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Get entities with associations
        TypedQuery<Trip> tripQuery = em.createQuery("SELECT DISTINCT t FROM Trip t WHERE t.id in (:ids)", Trip.class);
        tripQuery.setParameter("ids", tripIds);
        return tripQuery.getResultList();
    }

    @Override
    public int countByNameSearch(String name) {
        final TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Trip as t WHERE lower(t.name) LIKE lower(:name) AND t.isPrivate = false", Long.class);
        query.setParameter("name", "%" + name + "%");
        return query.getSingleResult().intValue();
    }

    @Override
    public void deleteTripPendingConfirmations(long tripId) {
        Query query = em.createQuery("DELETE TripPendingConfirmation AS tpc WHERE tpc.trip.id = :tripId");
        query.setParameter("tripId", tripId);
        query.executeUpdate();
    }

    @Override
    public int countAllPublicTrips() {
        TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Trip WHERE isPrivate = false", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public List<Trip> getAllTripsPerPage(int pageNum) {

        // Get primary keys with LIMIT and OFFSET
        final TypedQuery<Long> idQuery = em.createQuery("SELECT t.id FROM Trip t WHERE t.isPrivate = false order by t.startDate", Long.class);
        List<Long> tripIds = idQuery.setFirstResult((pageNum - 1) * MAX_ROWS)
                .setMaxResults(MAX_ROWS)
                .getResultList();

        if (tripIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Get entities with associations
        final TypedQuery<Trip> tripQuery = em.createQuery("SELECT DISTINCT t FROM Trip t WHERE t.id in (:ids)", Trip.class);
        tripQuery.setParameter("ids", tripIds);
        return tripQuery.getResultList();
    }

    @Override
    public TripPendingConfirmation createPendingConfirmation(Trip trip, User user, String token) {
        TripPendingConfirmation pendingConfirmation = new TripPendingConfirmation(trip, user, token);
        em.persist(pendingConfirmation);
        return pendingConfirmation;
    }

    @Override
    public List<TripPendingConfirmation> getTripJoinRequests(long tripId) {
        final TypedQuery<TripPendingConfirmation> query = em.createQuery("FROM TripPendingConfirmation as tp WHERE tp.trip.id = :tripId AND tp.edited = false", TripPendingConfirmation.class);
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
        Query query = em.createQuery("DELETE TripInvitation AS ti WHERE ti.token = :token AND ti.trip.id = :tripId");
        query.setParameter("token", token);
        query.setParameter("tripId", trip.getId());
        query.executeUpdate();
    }

    @Override
    public Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user) {
        final TypedQuery<TripInvitation> query = em.createQuery("FROM TripInvitation AS ti WHERE ti.trip.id = :tripId AND ti.invitee.id = :userId", TripInvitation.class);
        query.setParameter("tripId", trip.getId());
        query.setParameter("userId", user.getId());
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void updateTripData(String tripName, String description, long tripId) {
        Query query = em.createQuery("UPDATE Trip SET name = :tripName, description = :description WHERE id = :tripId");
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
        Query query = em.createQuery("UPDATE TripMember SET role = :adminRole WHERE trip.id = :tripId AND user.id = :userId");
        query.setParameter("adminRole", TripMemberRole.ADMIN);
        query.setParameter("tripId", tripId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void markTripAsCompleted(long tripId) {
        Query query = em.createQuery("UPDATE Trip SET status = :completed WHERE id = :tripId");
        query.setParameter("completed", TripStatus.COMPLETED);
        query.setParameter("tripId", tripId);
        query.executeUpdate();
    }

    @Override
    public void deleteTripMember(long userId, long tripId) {
        Query query = em.createQuery("DELETE TripMember WHERE trip.id = :tripId AND user.id = :userId");
        query.setParameter("tripId", tripId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void deleteAllTripMembers(long tripId) {
        Query query = em.createQuery("DELETE TripMember WHERE trip.id = :tripId");
        query.setParameter("tripId", tripId);
        query.executeUpdate();
    }

    @Override
    public void deleteTripInvitations(long tripId) {
        Query query = em.createQuery("DELETE TripInvitation WHERE trip.id = :tripId");
        query.setParameter("tripId", tripId);
        query.executeUpdate();
    }

    @Override
    public void deleteTrip(long tripId) {
        Query tripDelete = em.createQuery("DELETE Trip WHERE id = :tripId");
        tripDelete.setParameter("tripId", tripId);
        tripDelete.executeUpdate();
    }

    @Override
    public PaginatedResult<Trip> findWithFilters(Map<String, Object> filterMap, int page) {

        final TypedQuery<Long> idQuery = em.createQuery("SELECT distinct t.id FROM Trip as t, Place as p"
                + filtersQuery(filterMap) +  "order by t.id", Long.class);

        final TypedQuery<Long> queryCount = em.createQuery("select count(distinct t) From Trip as t, Place as p "
                + filtersQuery(filterMap), Long.class);

        setQueryParameters(idQuery, queryCount, filterMap);

        List<Long> tripIds = idQuery
                .setFirstResult((page - 1) * ADV_MAX_SEARCH_RESULTS)
                .setMaxResults(ADV_MAX_SEARCH_RESULTS)
                .getResultList();

        if (tripIds.isEmpty()) {
            return new PaginatedResult<>(new ArrayList<>(), 0);
        }

        TypedQuery<Trip> tripQuery = em.createQuery("SELECT DISTINCT t FROM Trip t WHERE t.id in (:ids)", Trip.class);
        tripQuery.setParameter("ids", tripIds);
        List<Trip> resultList = tripQuery.getResultList();

        int count = queryCount.getSingleResult().intValue();

        return new PaginatedResult<>(resultList, count);
    }

    private void setQueryParameters(TypedQuery<Long> query, TypedQuery<Long> countQuery, Map<String, Object> filterMap) {

        for (String filter : filterMap.keySet()) {
            if (filter.equals("place")) {
                query.setParameter("placeName", "%" + filterMap.get(filter) + "%");
                countQuery.setParameter("placeName", "%" + filterMap.get(filter) + "%");
            }
            if (filter.equals("name")) {
                query.setParameter("name", "%" + filterMap.get(filter) + "%");
                countQuery.setParameter("name", "%" + filterMap.get(filter) + "%");
            }
            if (filter.equals("startDate")) {
                query.setParameter("startDate", filterMap.get(filter));
                countQuery.setParameter("startDate", filterMap.get(filter));
            }
            if (filter.equals("endDate")) {
                query.setParameter("endDate", filterMap.get(filter));
                countQuery.setParameter("endDate", filterMap.get(filter));
            }
        }
    }

    private String filtersQuery(Map<String, Object> filterMap) {
        StringBuilder buffer = new StringBuilder();

        if (filterMap.containsKey("place")) {
            buffer.append(", Activity as a ");
        }
        buffer.append(" where t.isPrivate = false ");

        for (String filter : filterMap.keySet()) {
            switch (filter) {
                case "place":
                    buffer.append(" and ((t.startPlace.id = p.id and (lower(p.address) like lower(:placeName) or lower(p.name) like lower(:placeName)) )");
                    buffer.append("or (a.trip.id = t.id and ( lower(a.place.name) like lower(:placeName) or lower(a.place.address) like lower(:placeName))))");
                    break;
                case "name":
                    buffer.append(" and lower(t.name) LIKE lower(:name)");
                    break;
                case "startDate":
                    buffer.append(" and (t.startDate >= :startDate)");
                    break;

                case "endDate":
                    buffer.append(" and (t.endDate <= :endDate)");
                    break;
            }
        }
        return buffer.toString();
    }

    @Override
    public List<TripComment> getTripComments(long tripId) {
        final TypedQuery<TripComment> query = em.createQuery("FROM TripComment as tc where tc.trip.id = :tripId", TripComment.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }

    @Override
    public PaginatedResult<Trip> findUserTrips(long userId, int page) {

        // Get primary keys with LIMIT and OFFSET
        final TypedQuery<Long> idQuery = em.createQuery("SELECT t.id FROM Trip as t left join t.members as m" +
                " left join m.user as u where u.id = :userId order by t.startDate", Long.class);
        List<Long> tripIds = idQuery.setParameter("userId", userId)
                .setFirstResult((page - 1) * MAX_ROWS)
                .setMaxResults(MAX_ROWS)
                .getResultList();

        if (tripIds.isEmpty()) {
            return new PaginatedResult<>(new ArrayList<>(), 0);
        }

        // Get entities with associations
        TypedQuery<Trip> tripQuery = em.createQuery("SELECT DISTINCT t FROM Trip as t WHERE t.id in (:ids)", Trip.class);
        tripQuery.setParameter("ids", tripIds);
        List<Trip> result = tripQuery.getResultList();

        // Count query
        final TypedQuery<Long> queryCount = em.createQuery( "select count(distinct t) FROM Trip as t left join t.members as m" +
                " left join m.user as u where u.id = :userId", Long.class );
        queryCount.setParameter("userId", userId );
        int total = queryCount.getSingleResult().intValue();

        return new PaginatedResult<>(result, total);
    }

    @Override
    public List<TripMember> getTripMembers(long tripId) {
        final TypedQuery<TripMember> query = em.createQuery("FROM TripMember as tm where tm.trip.id = :tripId", TripMember.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }

    @Override
    public Boolean hasImage(long tripId) {
        final TypedQuery<TripPicture> query = em.createQuery("FROM TripPicture as tp WHERE tp.trip.id = :tripId", TripPicture.class);
        query.setParameter("tripId", tripId);
        return !query.getResultList().isEmpty();
    }

}
