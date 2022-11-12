<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String id = (String)session.getAttribute("id");
    String name = (String)session.getAttribute("name");
    String md = (String)session.getAttribute("md");
    boolean isLogin = (id != null) & (name != null);
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
            <div>
                <jsp:include page='./component/header/header.jsp'>
                    <jsp:param name="id" value="<%=id%>"/>
                    <jsp:param name="name" value="<%=name%>"/>
                    <jsp:param name="md" value="<%=md%>"/>
                </jsp:include>
            </div>
            <div>
                <% if(isLogin) { %>
                    <div class="index">
                        <div class="bg"></div>
                        <div class="index-title">2022학년도 겨울계절학기 수강 안내</div>
                        <div class="index-contents">
                            <p style="font-weight: bold;"><2022.11.11. 공지></p>
                            <p><span style="background-color: yellow;">[계열별 전문학술영어] 운영에 따른 안내</span></p>
                            <ul class="index-ul">
                                <li>2022년 2학기 계열별 전문학술영어 시행에 따라, 계절학기에도 계열벌로 수업이 운영됨.</li>
                                <li>수강신청화면에서 수업번호별 계열표시 확인 후 소속 계열에 해당하는 수업 신청 바람.</li>
                            </ul>
                            <p style="font-weight: bold;"><2022.11.07. 공지></p>
                            <p style="margin-bottom: 2vh;">2022학년도 서울캠퍼스 겨울계절학기는 대면강의로 진행됩니다.</p>
                            <ul class="index-ul">
                                <li>
                                    수강신청 사이트 바로가기
                                    <a href="https://portal.hanyang.ac.kr/sugang/sulg.do">
                                        https://portal.hanyang.ac.kr/sugang/sulg.do
                                    </a>
                                </li>
                                <li>
                                    한양대학교 공지사항 페이지
                                    <a href="http://www.hanyang.ac.kr/surl/XDRkB">
                                        http://www.hanyang.ac.kr/surl/XDRkB
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </body>
</html>