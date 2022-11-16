<%--
  Created by IntelliJ IDEA.
  User: wonnx
  Date: 2022/11/13
  Time: 2:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = request.getParameter("id");
    String name = request.getParameter("name");
    String md = request.getParameter("md");
    String base_URL = "http://localhost:8080/project1_war_exploded/";
    if(id.equals("null")) id=null;
    if(name.equals("null")) id=null;
    if(md.equals("null")) id=null;
    boolean mode = false;
    boolean isLogin = (id != null) & (name != null);
    if(isLogin){
        mode = md.equals("1");
    }
%>
<html>
    <body>
        <div class="top-bar-1">
            <span class="header-title" onclick="location.href='<%=base_URL + "index.jsp"%>'">
                <% if(isLogin) { %>
                    한양대학교 수강신청 페이지 | 2022년 겨울학기 수강신청
                <% } else { %>
                    한양대학교 수강신청 페이지
                <% } %>
            </span>
            <% if(isLogin) { %>
                <span class="header-logout-btn" onclick="location.href='<%=base_URL + "component/pages/login/logout.jsp"%>'">로그아웃</span>
            <% } else { %>
                <span class="header-login-btn" onclick="showPopup();">로그인</span>
            <% } %>
        </div>
        <div class="top-bar-2">
            <div>
                <% if(!isLogin) { %>
                    <span class="header-btn" onclick="location.href='<%=base_URL + "component/pages/handbook/handbook.jsp"%>'">수강편람</span>
                <% } %>
                <% if(isLogin) { %>
                    <% if(mode) { %>
                        <span class="header-btn" onclick="location.href='<%=base_URL + "component/pages/handbook/handbook.jsp"%>'">수강편람</span>
                        <span class="header-btn" onclick="location.href='<%=base_URL + "component/pages/enrolment/enrolment.jsp"%>'">수강신청</span>
                        <span class="header-btn" onclick="location.href=''">희망수업</span>
                        <span class="header-btn" onclick="location.href=''">신청내역</span>
                    <% } else { %>
                        <span class="header-btn" onclick="location.href=''">학생정보</span>
                        <span class="header-btn" onclick="location.href=''">과목정보</span>
                        <span class="header-btn" onclick="location.href=''">과목설강</span>
                        <span class="header-btn" onclick="location.href=''">과목폐강</span>
                    <% } %>
                <% } %>
            </div>
        <% if(isLogin) { %>
            <span class="header-user-btn">
                <% if(md.equals("0")) { %>
                    <%="관리자: " + name + "님"%>
                <% } else { %>
                    <%="사용자: " + name + "님"%>
                <% } %>
            </span>
        <% } %>
    </body>
    <script>
        function showPopup(){
            window.open("<%=base_URL + "component/pages/login/login.jsp"%>", "login", "width=750, height=300, left=200, top=200");
            return false;
        }
    </script>
</html>
