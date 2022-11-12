<%--
  Created by IntelliJ IDEA.
  User: wonnx
  Date: 2022/11/06
  Time: 1:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="utf-8">
        <title>user login</title>
        <link rel="stylesheet" href="../index.css?after">
    </head>
    <body>
        <div class="top-bar-1 vh17 p-v1 bc-f0">
            <span class="header-title cb"><%="사용자 로그인"%></span>
            <span class="header-close-btn cb" onclick="self.close();"></span>
        </div>
        <div class="container w100 vh83">
            <div class="login-form">
                <div class="login-form-left">
                    <form class="login-fields" method="post"
                          action="/project1_war_exploded/login/LoginServlet" id="login-form">
                        <input name="md" type="hidden" value="1"/>
                        <input name="id" class="login-form-field" type="text" placeholder="아이디"/>
                        <input name="pw" class="login-form-field" type="password" placeholder="비밀번호"/>
                    </form>
                </div>
                <div class="login-form-right">
                    <button class="login-submit-button" type="submit" form="login-form"><%="로그인"%></button>
                </div>
            </div>
        </div>
    </body>
</html>
