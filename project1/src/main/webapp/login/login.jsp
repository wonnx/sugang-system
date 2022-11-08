<%--
  Created by IntelliJ IDEA.
  User: wonnx
  Date: 2022/11/05
  Time: 5:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="utf-8">
        <title>login</title>
        <link rel="stylesheet" href="../index.css?after">
    </head>
    <body>
        <div class="top-bar-1 vh15 p-v1">
            <span class="header-title"><%="로그인"%></span>
            <span class="header-close-btn cw" onclick="self.close();"></span>
        </div>
        <div class="top-bar-2 vh2 p0"></div>
        <div class="container w100 vh83">
            <div class="login-box bc-ba" onclick="location.href='adminLogin.jsp';">관리자 로그인</div>
            <div class="login-box bc-f0" onclick="location.href='userLogin.jsp';">사용자 로그인</div>
        </div>
    </body>
</html>
