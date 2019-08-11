<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <c:url value="/resources/icons/mountainbg.jpg" var="defaultTP"/>
    <%@include file="head.jsp" %>
    <title><spring:message code="trips.title"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid mt-4" >
    <c:choose>
        <c:when test="${isEmpty}">
            <p class="alert alert-warning">
                <spring:message code="trips.noTrips"/>
            </p>
        </c:when>
        <c:otherwise>
            <div class="card-deck">
                <div class="row justify-content-center">
                    <c:forEach items="${userTripsList}" var="trip">
                        <c:url value="/home/trip/${trip.key.id}" var="tripUrl"/>
                        <a href="${tripUrl}" class="custom-card">
                            <div class="col-auto mb-3">
                                <div class="card trip-card" style="width: 30rem;">
                                    <c:url value="/home/trip/${trip.key.id}/image" var="tripImageURL"/>
                                    <c:choose>
                                        <c:when test="${trip.value.value.booleanValue()}">
                                            <img class="card-img-top" src="${tripImageURL}" height="250"
                                                 width="400">
                                        </c:when>
                                        <c:otherwise>
                                            <img class="card-img-top" src="${defaultTP}" height="250" width="400">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="card-body">
                                        <h5 class="card-title"><c:out value="${trip.key.name}"/></h5>
                                        <p class="card-text"><c:out value="${trip.key.description}"/></p>
                                        <p class="card-text"><i class="fas fa-map-marker-alt"></i> <c:out
                                                value="${trip.value.key.name}"/></p>
                                        <p class="card-text"><i
                                                class="far fa-calendar-alt"></i> ${trip.key.startDate.format(formatter)}
                                            -
                                                ${trip.key.endDate.format(formatter)}</p>
                                    </div>
                                    <div class="card-footer">
                                        <i class="fas fa-users"></i>
                                        <small class="text-muted">
                                            <c:out value="${trip.key.users.size() + 1}"/></small>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<div class="d-flex justify-content-center align-bottom">
    <ul class="pagination">
        <c:forEach var="i" begin="1" end="${pageQty}">
            <c:url value="/home/trips/${i}" var="tripsPageUrl"/>
            <li class="page-item"><a class="page-link" href="${tripsPageUrl}">${i}</a></li>
        </c:forEach>
    </ul>
</div>
<script>
    $(document).ready(function () {
        $('#my-trips-item').addClass('active');
    });
</script>
</body>
</html>
