<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="head.jsp"%>
    <c:url value="/resources/icons/defaultPP.png" var="defaultPP"/>
    <c:url value="/home/profile/${user.id}/edit" var="editProfile"/>
    <link href="${bootstrapCss}" rel="stylesheet">
    <link rel="shortcut icon" href="${iconURL}" type="image/x-icon"/>
    <title>${user.firstname} edit profile</title>
</head>
<body>
<jsp:include page="header.jsp"/>
    <div class="container ">
        <h3 class="display-4" style="margin-top: 20px;">Edit profile</h3>
        <br>
        <form:form action="${editProfile}" method="post" modelAttribute="editProfileForm" enctype="multipart/form-data" >
            <p>Upload profile picture</p>
            <form:errors path="imageUpload" cssClass = "alert alert-warning" element="p"/>
            <form:input type="file" path="imageUpload" accept = "image/*"/>
            <br>
            <br>
            <form:errors path="biography" cssClass = "alert alert-warning" element="p"/>
            <form:textarea rows="10"  cols="100" placeholder="Tell something about yourself..." path="biography"  maxlength="500" />
            <br>
            <button type="submit" class="btn btn-success text-center justify-content-center">Apply changes</button>
        </form:form>
        <c:if test="${fileSizeError}">
            <p class=" alert alert-warning">File size too big</p>
        </c:if>
        <c:if test="${invalidContentError}">
            <p class=" alert alert-warning">Invalid content. File must be jpg or png format</p>
        </c:if>
        <c:if test="${generalError}">
            <p class=" alert alert-warning">Error uploading file</p>
        </c:if>
    </div>
</body>
</html>