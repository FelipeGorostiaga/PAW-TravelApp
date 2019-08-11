<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <c:url value="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" var="bootstrapCss"/>
    <c:url value="/webjars/jquery/3.2.1/jquery.min.js" var="jquery" />
    <c:url value="/webjars/bootstrap/4.1.3/js/bootstrap.min.js" var="bootjs"/>
    <c:url value="/resources/icons/globe.ico" var="iconURL"/>
    <c:url value="/resources/css/404.css" var="cssStyle"/>
    <c:url value="/home/1" var="homeURL"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link href="${cssStyle}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900|Roboto:100,300,400,500,700,900" rel="stylesheet" />
    <title>Error 500</title>
</head>
<body>
<div class="container">
    <div class="error-wrapper">
        <div class="man-icon"></div>
        <h3 class="title">500</h3>
        <p class="info"><spring:message code="500.message"/></p>
        <a id="homeButton" class="btn btn-outline-success my-2 my-sm-0" href="${homeURL}"><spring:message code="500.homeBtn"/></a>
    </div>
</div>
<script src="${jquery}" type="text/javascript"></script>
<script src="${bootjs}" type="text/javascript"></script>
</body>
</html>