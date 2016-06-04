<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="body" fragment="true" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="${pageContext.request.locale}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet"/>

        <jsp:invoke fragment="head"/>

        <title>Group Manager</title>
    </head>
    <body>

        <nav class="header navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">
                        <img src="<c:url value="/resources/img/logo.png"/>" alt="logo"/> Group manager
                    </a>
                    <p class="navbar-text">(server side)</p>
                </div>
                <p class="navbar-text navbar-right">${perunPrincipal.user.displayName}</p>
            </div>
        </nav>


        <div class="content container">
            <div class="row">
                <div class="col-sm-12">

            <jsp:invoke fragment="body"/>

                </div>
            </div>
        </div>


        <nav class="footer navbar navbar-default">
            <div class="container">
                <p class="navbar-text">Ondřej Velíšek</p>
            </div>
        </nav>

    </body>
</html>
