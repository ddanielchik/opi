<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${userLocale}" />
<fmt:setBundle basename="messages" var="msg" />

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/error.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title><fmt:message key="error.404.title" bundle="${msg}" /></title>
</head>
<body>
<header>
    <h1><fmt:message key="error.404.title" bundle="${msg}" /></h1>
</header>

<main>
    <div class="error-content">
        <p><fmt:message key="error.404.message" bundle="${msg}" /></p>
        <a href="${pageContext.request.contextPath}/home.xhtml">
            <fmt:message key="error.homeLink" bundle="${msg}" />
        </a>
    </div>
</main>
</body>
</html>