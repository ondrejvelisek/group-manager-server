<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>


<my:layout>
    <jsp:attribute name="body">

        <div class="panel panel-danger">
            <div class="panel-heading">
                <h3 class="panel-title">
                    ${e.name}
                </h3>
            </div>
            <div class="panel-body">
                ${e.message}
            </div>
        </div>

    </jsp:attribute>
</my:layout>