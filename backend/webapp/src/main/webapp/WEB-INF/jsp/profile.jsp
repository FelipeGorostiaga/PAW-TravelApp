<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <c:url value="/home/profile/${user.id}/edit" var="editProfilePicture"/>
    <c:url value="/resources/css/tab.css" var="navCSS"/>
    <link href="${navCSS}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title><spring:message code="profile.title" arguments="${userProfile.firstname}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<section id="tabs" class="project-tab">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <nav>
                    <div class="nav nav-tabs nav-fill" id="nav-tab" role="tablist">
                        <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><spring:message code="profile.Info"/></a>
                        <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false"><spring:message code="profile.Trips"/></a>
                    </div>
                </nav>
                <div class="tab-content" id="nav-tabContent">
                    <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                        <div class="card car mx-auto" style="width: 60rem;">
                            <c:choose>
                                <c:when test="${hasProfilePicture}">
                                    <c:url value="/home/profile/${userProfile.id}/image" var="profilePictureURL"/>
                                    <img class="card-img-top" src="${profilePictureURL}">
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top" src="${defaultPP}" height="700" width="500">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-header">
                                <h5 class="card-title"><c:out value="${userProfile.firstname}"/> <c:out
                                        value="${userProfile.lastname}"/><span class="float-right"><i class="fas fa-map-pin"></i>  <c:out value="${userProfile.nationality}"/></span> </h5>
                            </div>
                            <div class="card-body">
                                <p class="card-text"><c:out value="${userProfile.biography}"/></p>
                                <p class="card-text"><i class="fas fa-envelope"></i>  <c:out value="${userProfile.email}"/></p>
                                <p class="card-text"><i class="fas fa-birthday-cake"></i>  <c:out value="${birthday}"/></p>
                                <c:if test="${user.id == userProfile.id}">
                                    <div class="card-footer">
                                        <a href="${editProfilePicture}" class="btn btn-primary text-white">
                                            <spring:message code="profile.editProfileBtn"/>
                                        </a>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                        <div class="list-group">
                            <c:forEach items="${trips}" var="trip">
                                <c:url value="/home/trip/${trip.id}/" var="tripURL"/>
                                <a href="${tripURL}" class="list-group-item list-group-item-action">
                                <div class="card">
                                    <div class="card-header">
                                        <i class="fas fa-users"></i> <c:out value="${trip.users.size() + 1}"/>
                                    </div>
                                    <div class="card-body">
                                        <h5 class="card-title"><c:out value="${trip.name}"/></h5>
                                        <p class="card-text"><c:out value="${trip.description}"/></p>
                                    </div>
                                </div>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>