<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <c:url value="/resources/icons/mountainbg.jpg" var="defaultTP"/>
    <c:url value="/resources/css/home.css" var="homeCSS"/>
    <%@include file="head.jsp" %>
    <link href="${homeCSS}" rel="stylesheet">
    <title><spring:message code="home.title"/></title>
</head>
<body>
<%@include file="header.jsp" %>

<div class="container-fluid mt-4">
    <div class="card-deck">
        <div class="row justify-content-center">
            <c:forEach items="${tripList}" var="trip">
                <c:url value="/home/trip/${trip.key.key.id}" var="tripUrl"/>
                <a href="${tripUrl}" class="custom-card">
                    <div class="col-auto mb-3">

                        <div class="card trip-card" style="width: 30rem;">
                            <c:url value="/home/trip/${trip.key.key.id}/image" var="tripImageURL"/>
                            <c:choose>
                                <c:when test="${trip.key.value}">
                                    <img class="card-img-top" src="${tripImageURL}" height="250" width="400">
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top" src="${defaultTP}" height="250" width="400">
                                </c:otherwise>
                            </c:choose>

                            <div class="card-body">
                                <h5 class="card-title"><c:out value="${trip.key.key.name}"/></h5>
                                <p class="card-text"><c:out value="${trip.key.key.description}"/></p>
                                <p class="card-text"><i class="fas fa-map-marker-alt"></i> <c:out
                                        value="${trip.value.name}"/></p>
                                <p class="card-text"><i
                                        class="far fa-calendar-alt"></i> ${trip.key.key.startDate.format(dateFormat)} -
                                        ${trip.key.key.endDate.format(dateFormat)}</p>
                            </div>

                            <div class="card-footer">
                                <i class="fas fa-users"></i>
                                <small class="text-muted">
                                    <c:out value="${trip.key.key.users.size() + 1}"/></small>
                            </div>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>

<div class="d-flex justify-content-center align-bottom">
    <ul class="pagination">
        <c:forEach var="i" begin="1" end="${pages}">
            <c:url value="/home/${i}" var="homepageURL"/>
            <li class="page-item"><a class="page-link" href="${homepageURL}">${i}</a></li>
        </c:forEach>
    </ul>
</div>

<script>
    $(document).ready(function () {
        $('#home-item').addClass('active');
    });
</script>
</body>
</html>