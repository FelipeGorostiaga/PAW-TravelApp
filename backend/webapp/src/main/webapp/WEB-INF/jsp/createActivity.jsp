<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/home/trip/${tripId}/create-activity" var="postURL"/>
    <c:url value="/resources/css/map.css" var="createTripCss"/>
    <c:url value="/resources/js/map.js" var="createTripJs"/>
    <c:url value="/resources/js/createTripDatepicker.js" var="datePicker"/>
    <c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"
           var="datepickerJs"/>
    <c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"
           var="datepickerCss"/>
    <script type="text/javascript" src="${datepickerJs}"></script>
    <script type="text/javascript" src="${datePicker}"></script>
    <link rel="stylesheet" href="${datepickerCss}"/>
    <link rel="stylesheet" href="${createTripCss}">
    <title>
        <spring:message code="createActivity.title"/>
    </title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container box" style="margin-top: 20px;">
        <div class="row">
            <div class="col-12">
                <div class="form-group">
                    <div class="display-4 text-white">
                        <spring:message code="createActivity.pageTitle"/>
                    </div>
                </div>
                <form:form action="${postURL}" method="post" modelAttribute="activityForm">
                    <div class="form-group">
                        <form:errors path="name" cssClass="alert alert-warning" element="p"/>
                        <spring:message code="createActivity.namePh" var="namePh"/>
                        <form:input type="text" placeholder="${namePh}" path="name" cssClass="form-control"/>
                    </div>
                    <div class="form-group">
                        <form:errors path="category" cssClass="alert alert-warning" element="p"/>
                        <spring:message code="createActivity.categoryPh" var="categoryPh"/>
                        <form:select name="category" path="category" cssClass="form-control"
                                     cssStyle="margin-bottom: 10px;">
                            <option disabled selected><spring:message code="createActivity.select"/></option>
                            <option value="Cultural"><spring:message code="createActivity.cultural"/></option>
                            <option value="Sports"><spring:message code="createActivity.sports"/></option>
                            <option value="Social"><spring:message code="createActivity.social"/></option>
                            <option value="Romantic"><spring:message code="createActivity.romantic"/></option>
                            <option value="Adventure"><spring:message code="createActivity.adventure"/></option>
                            <option value="Family"><spring:message code="createActivity.family"/></option>
                            <option value="Friends"><spring:message code="createActivity.friends"/></option>
                            <option value="Business"><spring:message code="createActivity.business"/></option>
                            <option value="Relaxation"><spring:message code="createActivity.relaxation"/></option>
                        </form:select>
                    </div>
                    <c:if test="${invalidDates}">
                        <div class="alert alert-danger" role="alert">
                            <spring:message code="createActivity.invalidDates"/>
                        </div>
                    </c:if>
                    <c:if test="${intervalError}">
                        <div class="alert alert-danger" role="alert">
                            <spring:message code="createActivity.intervalError"/>
                        </div>
                    </c:if>

                    <%--START DATE INPUT--%>
                    <div class="form-group">
                        <form:errors path="startDate" cssClass="alert alert-warning" element="p"/>
                        <form:label path="startDate" for="startDate" cssClass="text-white">
                            <spring:message code="createActivity.startDate"/>
                        </form:label>
                        <form:input cssClass="form-control" type="text" placeholder="dd/mm/yyyy" name="startDate"
                                    id="startDate" path="startDate"/>
                    </div>

                    <%--END DATE INPUT--%>
                    <div class="form-group">
                        <form:errors path="endDate" cssClass="alert alert-warning" element="p"/>
                        <form:label path="endDate" for="endDate" cssClass="text-white">
                            <spring:message code="createActivity.endDate"/>
                        </form:label>
                        <form:input cssClass="form-control" type="text" placeholder="dd/mm/yyyy" name="endDate"
                                    id="endDate" path="endDate"/>
                    </div>


                    <form:errors path="placeInput" cssClass="alert alert-warning" element="p"/>
                    <spring:message code="createActivity.locationPh" var="locationPh"/>
                    <form:input id="pac-input" type="text" path="placeInput" placeholder="${locationPh}"
                                cssClass="form-control"/>

                    <c:if test="${errorMap}">
                        <div class="alert alert-danger" role="alert">
                            <spring:message code="createActivity.invalidPlace"/>
                        </div>
                    </c:if>
                    <div class="container" id="map"></div>
                    <div id="infowindow-content">
                        <img src="" width="16" height="16" id="place-icon">
                        <span id="place-name" class="title"></span><br>
                        <span id="place-address"></span>
                    </div>
                    <div class="text-center flex-fill">
                        <button type="submit" class="btn-success btn btn-lg">
                            <spring:message code="createActivity.createBtn"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script src="${createTripJs}"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M&libraries=places&callback=initMap"></script>
<script>
    $(document).ready(function () {
        $('#create-trip-item').addClass('active');
    });
</script>
</body>
</html>
