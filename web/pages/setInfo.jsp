<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>setInfo</title>
</head>
<body>
<form name="form" action="${ctx}/test/setInfo" method="post">
    <input name="info" value="info">
    <input type="submit">
</form>
</body>
</html>