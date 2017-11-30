<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-collapse collapse">
            <form:form class="navbar-form" role="form" action="spring_security_check" method="post">
                <div class="form-group">
                    <input type="text" placeholder="User Name" class="form-control" name="username">
                </div>
                <div class="form-group">
                    <input type="password" placeholder="Password" class="form-control" name="password">
                </div>
                <button type="submit" class="btn btn-success">
                    Login
                </button>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>