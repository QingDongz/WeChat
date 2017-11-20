<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/18
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信测试</title>
</head>
<body>
<c:forEach items="${users}" var="user">
    ${user.username}------${user.password}-------<br>
</c:forEach>
</body>
</html>
