<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<my:layout>
    <jsp:attribute name="body">

        <div class="row">
            <div class="col-md-6">




                <table class="table table-bordered table-hover">
                    <thead>

                    <tr>
                        <th colspan="2" class="toolbar-cell">
                            <ol class="breadcrumb">

                                <c:choose>
                                    <c:when test="${isVo}">
                                        <li class="active">${vo.name}</li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="/${vo.shortName}">${vo.name}</a></li>
                                    </c:otherwise>
                                </c:choose>

                                <c:forEach items="${breadcrumbs}" var="breadcrumb">

                                    <c:choose>
                                        <c:when test="${group.name eq breadcrumb.name}">
                                            <li class="active">${breadcrumb.shortName}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="/${vo.shortName}/${breadcrumb.name}">${breadcrumb.shortName}</a></li>
                                        </c:otherwise>
                                    </c:choose>

                                </c:forEach>

                                <c:if test="${isVo}"><c:set var="disabled" value="disabled"/></c:if>

                                <a href="/${vo.shortName}/${parentGroup.name}" class="pull-right ${disabled}">
                                    <i class="glyphicon glyphicon-arrow-up"></i>
                                </a>
                            </ol>
                        </th>
                    </tr>

                    <tr>
                        <th>Name</th><th>Description</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach items="${groups}" var="group">
                        <tr onclick="window.location.href='/${vo.shortName}/${group.name}'">
                            <td>
                                    ${group.shortName}
                            </td>
                            <td>
                                    ${group.description}
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>


            </div>
            <div class="col-md-6">

                <c:if test="${not isVo}">

                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>State</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${memberUsers}" var="entry">
                            <tr>
                                <td>
                                        ${entry.value.displayName}
                                </td>
                                <td>
                                        ${entry.key.status}
                                </td>
                                <td class="tight-cell btn-cell">
                                <c:choose>
                                    <c:when test="${isMembers}">
                                        <button class="btn btn-danger" disabled="disabled">
                                            <i class="glyphicon glyphicon-arrow-down"></i>
                                        </button>
                                    </c:when>
                                    <c:when test="${entry.key.membershipType eq 'DIRECT'}">
                                        <form:form action="/${vo.shortName}/${group.name}/remove-member/${entry.key.id}" method="POST">
                                            <button class="btn btn-danger" type="submit">
                                                <i class="glyphicon glyphicon-minus"></i>
                                            </button>
                                        </form:form>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-default" disabled="disabled">
                                            <i class="glyphicon glyphicon-empty"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>

                </c:if>



                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>State</th>
                        <c:if test="${not isVo}">
                            <th></th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach items="${voMemberUsers}" var="entry">
                        <tr>
                            <td>
                                ${entry.value.displayName}
                            </td>
                            <td>
                                ${entry.key.status}
                            </td>
                            <c:if test="${not isVo}">
                            <td class="tight-cell btn-cell">
                                <form:form action="/${vo.shortName}/${group.name}/add-member/${entry.key.id}" method="POST">
                                <button class="btn btn-success" type="submit">
                                    <i class="glyphicon glyphicon-plus"></i>
                                </button>
                                </form:form>
                            </td>
                            </c:if>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>


            </div>
        </div>



    </jsp:attribute>
</my:layout>