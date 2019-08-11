<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${trip.id}/delete" var="deleteTripURL"/>
    <c:url value="/home/trip/${trip.id}" var="backURL"/>
    <c:url value="/resources/css/trip.css" var="tripCSS"/>
    <c:url value="/home/trip/${trip.id}/edit" var="editTripURL"/>
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${tripCSS}" rel="stylesheet">
    <title><c:out value="${trip.name}"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container box">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                    <a class="btn btn-primary" style="margin-top: 15px;margin-bottom: 15px;" href="${backURL}"
                       role="button">
                        <i class="fas fa-arrow-circle-left"></i>
                       <%-- <spring:message code="editTrip.back"/>--%>
                    </a>

                    <form action="${deleteTripURL}" method="post">
                        <input type="submit" value="<spring:message code="trip.deleteTripBtn"/>" class="btn btn-danger btn-lg" >
                    </form>

                    <c:if test="${fileSizeError}">
                        <p class=" alert alert-warning">
                            <spring:message code="trip.sizeError"/>
                        </p>
                    </c:if>
                    <c:if test="${invalidContentError}">
                        <p class=" alert alert-warning">
                            <spring:message code="trip.contentError"/>
                        </p>
                    </c:if>
                    <c:if test="${generalError}">
                        <p class=" alert alert-warning">
                            <spring:message code="trip.generalError"/>
                        </p>
                    </c:if>
                    <form:form action="${editTripURL}" method="post" modelAttribute="editTripForm"
                               enctype="multipart/form-data">
                        <form:errors path="imageUpload" cssClass="alert alert-warning" element="p"/>
                        <form:input type="file" path="imageUpload" accept="image/*"
                                    cssClass="text-white form-control-file"/>
                        <br>
                        <button type="submit" class="btn btn-success" style="margin-top: 10px;">
                            <spring:message code="trip.addImgBtn"/>
                        </button>
                    </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>