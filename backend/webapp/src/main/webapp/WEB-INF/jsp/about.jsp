<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@include file="head.jsp"%>
    <c:url value="/resources/css/about.css" var="aboutCss"/>
    <link href="${aboutCss}" rel="stylesheet">
    <title><spring:message code="about.title"/></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid" id="main-container">
    <div class="container box">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div id="myHeader" class="display-4 text-white"><spring:message code="about.pageTitle"/></div>
                <ul class="list-group">
                    <li class="list-group-item"><spring:message code="about.felipe"/></li>
                    <li class="list-group-item"><spring:message code="about.ignacio"/></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#about-item').addClass('active');
    });
</script>
</body>
</html>
