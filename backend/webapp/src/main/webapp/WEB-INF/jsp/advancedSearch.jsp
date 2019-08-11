<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="head.jsp" %>
    <c:url value="/resources/js/createTripDatepicker.js" var="datePicker"/>
    <c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js" var="datepickerJs"/>
    <c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css" var="datepickerCss"/>
    <c:url value="/home/advanced-search/query" var="asearchURL"/>
    <script type="text/javascript" src="${datepickerJs}"></script>
    <script type="text/javascript" src="${datePicker}"></script>
    <link rel="stylesheet" href="${datepickerCss}"/>
    <title><spring:message code="advancedSearch.title"/></title>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid" id="main-container">
    <div class="container">
        <div class="list-group">
            <c:if test="${noInput}">
                <p class="alert alert-warning"><spring:message code="advancedSearch.noInput"/></p>
            </c:if>
            <form action="${asearchURL}" method="get" class="form-inline my-2 my-lg-0">

                <input id="placeName" name="placeName" class="form-control mr-sm-2" type="text"
                       placeholder="<spring:message code="advancedSearch.placeName"/>">

                <c:if test="${invalidStartDate || invalidEndDate}">
                    <p class="alert alert-warning"><spring:message code="advancedSearch.invalidDate"/></p>
                </c:if>

                <label path="startDate" for="startDate"><spring:message code="advancedSearch.startDate"/></label>
                <input id="startDate" name="startDate" class="form-control mr-sm-2" type="text"
                       placeholder="dd/mm/yyyy" aria-label="Search">

                <label path="endDate" for="endDate"><spring:message code="advancedSearch.endDate"/></label>
                <input id="endDate" name="endDate" class="form-control mr-sm-2" type="text"
                       placeholder="dd/mm/yyyy" aria-label="Search">

                <select name="category" path="category" id="category" cssClass="form-control mr-sm-2" cssStyle="margin-bottom: 10px;">
                    <option value="NA" selected><spring:message code="createActivity.dropdown"/></option>
                    <option value="Cultural"><spring:message code="createActivity.cultural"/></option>
                    <option value="Sports"><spring:message code="createActivity.sports"/></option>
                    <option value="Social"><spring:message code="createActivity.social"/></option>
                    <option value="Romantic"><spring:message code="createActivity.romantic"/></option>
                    <option value="Adventure"><spring:message code="createActivity.adventure"/></option>
                    <option value="Family"><spring:message code="createActivity.family"/></option>
                    <option value="Friends"><spring:message code="createActivity.friends"/></option>
                    <option value="Business"><spring:message code="createActivity.business"/></option>
                    <option value="Relaxation"><spring:message code="createActivity.relaxation"/></option>
                </select>
                <button class="btn btn-success my-2 my-sm-0" type="submit"><spring:message code="header.searchBtn"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>