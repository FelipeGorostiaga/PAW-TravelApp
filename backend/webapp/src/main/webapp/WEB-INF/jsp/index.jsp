<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<%@include file="head.jsp"%>
	<c:url value="/resources/css/index.css" var="indexCSS"/>
	<c:url value="/signup" var="signUpUrl"/>
	<link href="${indexCSS}" rel="stylesheet">
	<title>Meet and Travel</title>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<div class="container-fluid" id="main-container">
		<div class="container box">
			<h1 class="text-white"><spring:message code="index.title"/></h1>
			<p class="text-white"><spring:message code="index.greeting"/></p>
			<a class="btn btn-success align-content-center" href="${signUpUrl}"><spring:message code="index.signUp"/></a>
		</div>
	</div>
</body>
</html>