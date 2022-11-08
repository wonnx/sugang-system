<%--
  Created by IntelliJ IDEA.
  User: wonnx
  Date: 2022/11/06
  Time: 11:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  session.invalidate();
  response.sendRedirect("/project1_war_exploded/index.jsp");
%>
