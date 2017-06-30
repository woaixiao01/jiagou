<%@ page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE HTML>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="base" value="/"></c:url>

<%String currentTime = (String)request.getAttribute("currentTime");


%>

<html>
<head>


<head>
<title>Hello</title>
</head>
<body>
<h1>Hello!</h1>
<h2>当前时间：<%=currentTime %></h2>
<h2>当前时间：${currentTime}</h2>
</body>
</html>