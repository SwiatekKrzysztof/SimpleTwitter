<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = " java.util.* " %>
<%@ page isELIgnored="false"%>

<html>
<head>
    <title>Title</title
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<h2>

    <br>
    <strong class="d-block text-gray-dark">${requestScope.user.name}</strong>
    <br>
    <strong class="d-block text-gray-dark">${user.email}</strong>
    <br>
    <strong class="d-block text-gray-dark">${user}</strong>

</h2>

</body>
</html>
