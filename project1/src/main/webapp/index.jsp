<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String id = (String)session.getAttribute("id");
    String name = (String)session.getAttribute("name");
//    System.out.println("index.jsp " + id + " " + name);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>enrolment system</title>
        <link rel="stylesheet" href="./index.css?after">
    </head>
    <body>
        <div>
            <div class="top-bar-1">
                <span class="header-title"><%="한양대학교 수강신청 페이지"%></span>
                <% if (id == null | name == null) { %>
                    <span class="header-login-btn" onclick="showPopup();"><%="로그인"%></span>
                <% } else { %>
                    <span class="header-logout-btn" onclick="location.href='./login/logout.jsp'"><%=name+"님 로그아웃"%></span>
                <% } %>
            </div>
            <div class="top-bar-2">
                <span class="header-handbook-btn" onclick="location.href='test'"><%="수강편람"%></span>
            </div>
        </div>
        <script>
            function showPopup(){
                window.open("./login/login.jsp", "login", "width=750, height=300, left=200, top=200");
                return false;
            }
        </script>
    </body>
</html>