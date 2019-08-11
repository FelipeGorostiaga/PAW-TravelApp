<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${trip.id}/create-activity" var="createActivityURL"/>
    <c:url value="/home/trip/${trip.id}/join" var="joinTripURL"/>
    <c:url value="/home/trip/${trip.id}/exit" var="exitTripURL"/>
    <c:url value="/home/trip/${trip.id}/delete" var="deleteTripURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <c:url value="/resources/js/showMap.js" var="showMapJs"/>
    <c:url value="/home/trip/${trip.id}/edit" var="editTripURL"/>
    <c:url value="/home/trip/${trip.id}/image" var="tripImageURL"/>
    <c:url value="/home/trip/${trip.id}" var="commentUrl"/>
    <c:url value="/resources/css/tab.css" var="navCSS"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${tripCSS}" rel="stylesheet">
    <link href="${navCSS}" rel="stylesheet">
    <title><c:out value="${trip.name}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>

<section id="tabs" class="project-tab">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <nav>
                    <div class="nav nav-tabs nav-fill" id="nav-tab" role="tablist">
                        <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><spring:message code="trip.tabInfo"/></a>
                        <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false"><spring:message code="trip.tabActivities"/></a>
                        <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab" aria-controls="nav-contact" aria-selected="false"><spring:message code="trip.tabComments"/></a>
                    </div>
                </nav>
                <div class="tab-content" id="nav-tabContent">
                    <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                        <c:if test="${hasTripPicture}">
                            <div class="img-container" style="background-image: url(${tripImageURL});">

                            </div>
                        </c:if>
                        <div>
                            <div class="d-flex justify-content-between">
                                <c:if test="${user.id == admin.id}">
                                    <div class="p-2">
                                        <a href="${editTripURL}" class="btn btn-primary text-white align-middle" style="margin-top: 10px;">
                                            <i class="fas fa-edit "></i>
                                        </a>
                                    </div>
                                </c:if>
                            </div>
                            <div class="display-4 "><c:out value="${trip.name}"/></div>
                            <p class="text-justify" style="font-size: 150%;"><c:out value="${trip.description}"/></p>
                            <div id="rate-bar" class="card-text"style="font-size: 150%; margin-bottom: 15px;"></div>

                            <p class="card-text"><i class="far fa-calendar-alt"></i>  ${startDate.format(formatter)} -
                                ${endDate.format(formatter)}</p>
                        </div>

                        <h3 class="margin-class"><spring:message code="trip.startPlace"/></h3>
                        <ul class="list-group">
                            <button type="button"
                                    class="list-group-item list-group-item-action flex-column align-items-start"
                                    onclick="initMapStartPlace(${startPlace.latitude},${startPlace.longitude})">
                                <i class="fas fa-map-marker-alt"></i>
                                <c:out value="${startPlace.name}"/>
                            </button>
                            <div id="startPlaceMap" style="height: 400px;width: 97%;display: none;"></div>
                        </ul>
                        <h3 class="margin-class">
                            <spring:message code="trip.members"/>
                        </h3>
                        <div class="list-group">
                            <a class="list-group-item list-group-item-action flex-column align-items-start"
                               href="<c:url value='/home/profile/${admin.id}'/>">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1"><i class="fas fa-user" style="margin-right: 5px;"></i>
                                        <c:out value="${admin.firstname}"/> <c:out value="${admin.lastname}"/></h5>
                                    <small><spring:message code="trip.admin"/></small>
                                </div>
                            </a>
                            <c:forEach items="${users}" var="ur">
                                <a class="list-group-item list-group-item-action flex-column align-items-start"
                                   href="<c:url value='/home/profile/${ur.id}'/>">
                                    <div class="d-flex w-100 justify-content-between">
                                        <h5 class="mb-1"><i class="fas fa-user" style="margin-right: 5px;"></i>
                                            <c:out value="${ur.firstname}"/> <c:out value="${ur.lastname}"/></h5>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>

                        <c:if test="${user.id != admin.id}">
                            <c:choose>
                                <c:when test="${!isTravelling}">
                                    <form action="${joinTripURL}" method="post" style="margin-top: 20px;">
                                        <input type="submit" value="<spring:message code="trip.joinTripBtn"/>" class="btn btn-success btn-lg text-white" >
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${exitTripURL}" method="post" style="margin-top: 20px;">
                                        <input type="submit" value="<spring:message code="trip.exitTripBtn"/>" class="btn btn-danger btn-lg text-white" >
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>

                    <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                        <h3 class="margin-class ">
                            <spring:message code="trip.activities"/>
                        </h3>
                        <c:choose>
                            <c:when test="${isEmpty}">
                                <div class="alert alert-primary" role="alert">
                                    <spring:message code="trip.noActivities"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="list-group" style="margin-bottom: 20px">
                                    <c:forEach items="${actAndPlaces}" var="activity_places">
                                        <div class="container">
                                            <button type="button"
                                                    class="list-group-item list-group-item-action flex-column align-items-start"
                                                    onclick="initMap(${activity_places.key.id},
                                                        ${activity_places.value.latitude},${activity_places.value.longitude})">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1"><c:out value="${activity_places.key.name}"/></h5>
                                                </div>
                                                <small><c:out value="${activity_places.key.category}"/></small>
                                                <br>
                                                <small><c:out value="${activity_places.key.startDate.format(formatter)}"/> -
                                                    <c:out value="${activity_places.key.endDate.format(formatter)}"/>
                                                </small>
                                                <p class="mb-1"><i class="fas fa-map-marker-alt"></i>
                                                    <c:out value="${activity_places.value.address}"/></p>
                                            </button>

                                            <c:url value="/home/trip/${trip.id}/${activity_places.key.id}/delete" var="deleteActURL"/>
                                            <c:if test="${user.id == admin.id}">
                                                <form action="${deleteActURL}" method="post">
                                                    <button type="submit" class="btn float-right">
                                                        <i class="fas fa-trash-alt "></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                        <div id="map${activity_places.key.id}" style="height: 400px;width: 97%;display: none;"></div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${user.id == admin.id}">
                            <a class="btn btn-primary text-white" href="${createActivityURL}" role="button">
                                <spring:message code="trip.addActivityBtn"/>
                            </a>
                        </c:if>
                    </div>


                    <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                        <c:forEach items="${comments}" var="comment" >
                            <div class="form-group" style="margin-top: 25px;">
                                <strong><c:out value="${comment.user.firstname}"/> <c:out value="${comment.user.lastname}"/> </strong>
                                <c:out value="${comment.createdOn.format((timeFormatter))}"/><br> <c:out value="${comment.comment}"/>
                            </div>
                        </c:forEach>

                        <c:choose >
                            <c:when test="${isTravelling || admin.equals(user)}">
                                <form:form action="${commentUrl}" method="post" modelAttribute="tripCommentForm" >
                                    <div class="form-group">
                                        <form:textarea path="comment" cssClass="text-area form-control"/>
                                        <form:errors path="comment" cssClass="alert alert-warning" element="p"/>
                                    </div>
                                    <form:button type="submit" class="btn btn-primary">
                                        <i class="fas fa-paper-plane"></i>
                                    </form:button>
                                </form:form>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                   <spring:message code="trip.cannotComment"/>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
<script src="${showMapJs}"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M"></script>
<c:url value="/resources/js/rater.js" var="raterJs"/>
<script src="${raterJs}" charset="utf-8"></script>
<c:url value="/resources/js/rater-helper.js" var="raterHelperJs"/>
<script src="${raterHelperJs}" type="text/javascript"></script>

<c:url value="/home/trip/${trip.id}/rate/" var="ratePostUrl"/>
<script>
    init_rate("${ratePostUrl}", ${rate}, ${isTravelling || user.id == admin.id});
</script>
</html>