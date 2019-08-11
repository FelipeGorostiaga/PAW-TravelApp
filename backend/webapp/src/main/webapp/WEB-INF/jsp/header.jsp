<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:url value="/home/1" var="home"/>
<c:url value="/" var="index"/>
<c:url value="/about" var="about"/>
<c:url value="/home/trips/1" var="trips"/>
<c:url value="/home/create-trip" var="createTrip"/>
<c:url value="/resources/icons/earth-globe.png" var="globeIMG"/>
<c:url value="https://img.icons8.com/cotton/50/000000/detective.png" var="searchIMG"/>
<c:url value="/resources/icons/uu.png" var="userIMG"/>
<c:url value="/signin" var="signinURL"/>
<c:url value="/resources/css/header.css" var="headerCss"/>
<c:url value="/logout" var="logoutUrl"/>
<c:url value="/home/profile/${user.id}" var="profile"/>
<c:url value="/home/search-trip/" var="searchURL"/>
<c:url value="/home/advanced-search/" var="advancedSearchURL"/>
<c:url value="/resources/js/header.js" var="headerJs"/>
<script type="text/javascript" src="${headerJs}"></script>
<link href="${headerCss}" rel="stylesheet">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <c:choose>
    <c:when test="${user == null}">
    <a class="navbar-brand" href="${index}">
        <img src="${globeIMG}" height="42" width="42"/>
    </a>
    </c:when>
        <c:otherwise>
        <a class="navbar-brand" href="${home}">
            <img src="${globeIMG}" height="42" width="42"/>
        </a>
        </c:otherwise>
    </c:choose>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <%--id="navbarSupportedContent"--%>
    <div class="collapse navbar-collapse" id="topheader">
        <ul class="navbar-nav mr-auto">

        <c:if test="${user != null}">
            <li class="nav-item" id="home-item">
            <a class="nav-link" href="${home}"><spring:message code="header.home"/> <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
            </li>
            <li class="nav-item" id="my-trips-item">
            <a class="nav-link" href="${trips}"><spring:message code="header.myTrips"/></a>
            </li>
            <li class="nav-item" id="create-trip-item">
            <a class="nav-link" href="${createTrip}"><spring:message code="header.createTrip"/> </a>
            </li>
        </c:if>
            <li class="nav-item" id="about-item">
                 <a class="nav-link" href="${about}"><spring:message code="header.aboutUs"/> </a>
            </li>
        </ul>

        <c:if test="${user != null}">
        <a class="nav-item" href="${advancedSearchURL}">
            <img src="${searchIMG}" height="38" width="38" style="margin-right: 7px;">
        </a>
        <form action="${searchURL}" method="get" class="form-inline my-2 my-lg-0">
            <input id="nameInput" name="nameInput" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="header.searchMessage"/>"
                   aria-label="Search">
            <button class="btn btn-success my-2 my-sm-0" type="submit"><spring:message code="header.searchBtn"/></button>
        </form>
        </c:if>
        <c:choose>
            <c:when test="${user == null}">
                <a id="signinButton" style="margin-left: 15px" class="btn btn-success my-2 my-sm-0" href="${signinURL}"><spring:message code="header.signInBtn"/></a>
            </c:when>
            <c:otherwise>
                <a href="${profile}">
                    <img alt="" style="margin-left: 10px" src="${userIMG}" height="32" width="32"/>
                </a>
                <a style="margin-left: 10px" class="btn btn-dark my-2 my-sm-0" href="${logoutUrl}"><spring:message code="header.logout"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>