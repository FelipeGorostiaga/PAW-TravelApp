<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <title>
        <spring:message code="searchTrips.title"/>
    </title>
</head>
<body>
<%@include file="header.jsp"%>
    <div class="container">
        <h3 class="display-4 context-menu" style="margin-top: 20px;margin-bottom: 15px;">
            <spring:message code="searchTrips.results"/>
        </h3>
        <c:choose>
            <c:when test="${tripQty == 0}">
                <p class="alert alert-warning">
                    <spring:message code="searchTrips.noResults"/>
                </p>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                    <c:forEach items="${tripsList}" var="trip">
                        <c:url value="/home/trip/${trip.id}" var="tripUrl"/>
                        <a href="${tripUrl}" class="list-group-item list-group-item-action flex-column align-items-start">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">
                                    <c:out value="${trip.name}"/>
                                </h5>
                            </div>
                            <p class="mb-1"><c:out value="${trip.description}"/></p>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>