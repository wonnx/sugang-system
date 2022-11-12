<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)session.getAttribute("id");
    String name = (String)session.getAttribute("name");
    String md = (String)session.getAttribute("md");
%>
<html>
    <head>
        <meta charset="utf-8">
        <title>enrolment</title>
        <link rel="stylesheet" href="../../../index.css">
    </head>
    <body>
        <jsp:include page='../../header/header.jsp'>
            <jsp:param name="id" value="<%=id%>"/>
            <jsp:param name="name" value="<%=name%>"/>
            <jsp:param name="md" value="<%=md%>"/>
        </jsp:include>
    </body>
</html>
