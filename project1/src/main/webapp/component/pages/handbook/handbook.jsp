<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)session.getAttribute("id");
    String name = (String)session.getAttribute("name");
    String md = (String)session.getAttribute("md");
    boolean isLogin = (id != null) & (name != null);
    String class_no = (String)session.getAttribute("class_no");
    String course_id = (String)session.getAttribute("course_id");
    String course_name = (String)session.getAttribute("course_name");
    ArrayList<HashMap<String, Object>> course_list = new ArrayList<HashMap<String, Object>>();
    if(session.getAttribute("handbook") != null){
        course_list = (ArrayList<HashMap<String, Object>>) session.getAttribute("handbook");
        session.removeAttribute("handbook");
    }
%>
<html>
    <head>
        <meta charset="utf-8">
        <title>수강신청</title>
        <link rel="stylesheet" href="../../../index.css?after">
    </head>
    <body>
        <div>
            <div>
                <jsp:include page='../../header/header.jsp'>
                    <jsp:param name="id" value="<%=id%>"/>
                    <jsp:param name="name" value="<%=name%>"/>
                    <jsp:param name="md" value="<%=md%>"/>
                </jsp:include>
            </div>
            <div class="w100 vh84">
                <div class="handbook-title">> 수강편람</div>
                <div class="search-requirement">
                    <form id="handbook-form" class="handbook"
                          action="/project1_war_exploded/handbook/HandbookServlet" method="get">
                        <div class="handbook-left-div">
                            <input name="page" type="hidden" value="handbook">
                            <div class="handbook-left-box">
                                <div class="handbook-form-title">수업번호</div>
                                <input name="class_no" type="text"
                                       class="handbook-form-field" value=<%=class_no != null ? class_no : ""%>>
                            </div>
                            <div class="handbook-left-box">
                                <div class="handbook-form-title">학수번호</div>
                                <input name="course_id" type="text"
                                       class="handbook-form-field" value=<%=course_id != null ? course_id : ""%>>
                            </div>
                            <div class="handbook-left-box">
                                <div class="handbook-form-title">교과목명</div>
                                <input name="course_name" type="text"
                                       class="handbook-form-field" value=<%=course_name != null ? course_name : ""%>>
                            </div>
                        </div>
                        <div class="handbook-right-div">
                            <button class="handbook-init-btn bc-d9" type="reset" form="handbook-form"
                                    onclick="<% session.setAttribute("class_no", "");
                                                session.setAttribute("course_id", "");
                                                session.setAttribute("course_name", "");
                                             %>; location.reload();">
                                초기화
                            </button>
                            <button class="handbook-submit-btn bc-f0" type="submit" form="handbook-form">조회</button>
                        </div>
                    </form>
                </div>
                <div class="w100 vh4"><%-- blank --%></div>
                <div class="w100 vh56">
                    <div class="handbook-list">
                        <div class="handbook-list-title">
<%--                            <div class="handbook-list-title-name w8">신청</div>--%>
                            <div class="handbook-list-title-name w10">수업번호</div>
                            <div class="handbook-list-title-name w10">학수번호</div>
                            <div class="handbook-list-title-name w17">교과목명</div>
                            <div class="handbook-list-title-name w12">교강사이름</div>
                            <div class="handbook-list-title-name w25">수업시간</div>
                            <div class="handbook-list-title-name w10">수강/정원</div>
                            <div class="handbook-list-title-name w16">강의실</div>
                        </div>
                        <% if(course_list.size() != 0) { %>
                            <div class="handbook-list-content">
                                <% for(int idx=0; idx<course_list.size(); idx++) { %>
                                    <% HashMap<String, Object> course = course_list.get(idx); %>
                                    <div class="handbook-list-content-items">
<%--                                        <div class="handbook-list-content-item w8">--%>
<%--                                            <button class="handbook-btn apply-btn" <% if(!isLogin) { %> disabled <% } %> >신청</button>--%>
<%--                                        </div>--%>
                                        <div class="handbook-list-content-item w10"><%=course.get("수업번호")%></div>
                                        <div class="handbook-list-content-item w10"><%=course.get("학수번호")%></div>
                                        <div class="handbook-list-content-item w17"><%=course.get("교과목명")%></div>
                                        <div class="handbook-list-content-item w12"><%=course.get("교강사이름")%></div>
                                        <div class="handbook-list-content-item w25">
                                            <%=course.get("수업시간") != null ? course.get("수업시간") : ""%>
                                        </div>
                                        <div class="handbook-list-content-item w10"><%=course.get("수강정원")%></div>
                                        <div class="handbook-list-content-item w16"><%=course.get("강의실")%></div>
                                    </div>
                                <% } %>
                            </div>
                        <% } else { %>
                            <div class="handbook-list-content vh8">
                                <span>조회를 하지 않았거나 조회된 데이터가 없습니다.</span>
                            </div>
                        <% } %>
                        <div class="handbook-list-bottom"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
