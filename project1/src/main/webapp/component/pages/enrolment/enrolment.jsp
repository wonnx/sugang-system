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
        <link rel="stylesheet" href="../../../index.css">
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    </head>
    <body>
        <div>
            <jsp:include page='../../header/header.jsp'>
                <jsp:param name="id" value="<%=id%>"/>
                <jsp:param name="name" value="<%=name%>"/>
                <jsp:param name="md" value="<%=md%>"/>
            </jsp:include>
        </div>
        <div>
            <div class="w100 vh84">
                <div class="handbook-title">> 수강신청</div>
                <div class="search-requirement">
                    <form id="handbook-form" class="handbook"
                          action="/project1_war_exploded/handbook/HandbookServlet" method="get">
                        <div class="handbook-left-div">
                            <input name="page" type="hidden" value="enrolment">
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
                            <div class="handbook-list-title-name w8">담기</div>
                            <div class="handbook-list-title-name w8">신청</div>
                            <div class="handbook-list-title-name w8">수업번호</div>
                            <div class="handbook-list-title-name w8">학수번호</div>
                            <div class="handbook-list-title-name w13">교과목명</div>
                            <div class="handbook-list-title-name w8">교강사이름</div>
                            <div class="handbook-list-title-name w23">수업시간</div>
                            <div class="handbook-list-title-name w10">신청/수강</div>
                            <div class="handbook-list-title-name w14">강의실</div>
                        </div>
                        <% if(course_list.size() != 0) { %>
                        <div class="handbook-list-content">
                            <% for(int idx=0; idx<course_list.size(); idx++) { %>
                                <% HashMap<String, Object> course = course_list.get(idx); %>
                                <div class="handbook-list-content-items">
                                    <div class="handbook-list-content-item w8">
                                        <button class="handbook-btn save-btn" <% if(!isLogin) { %> disabled <% } %>>담기</button>
                                    </div>
                                    <div class="handbook-list-content-item w8">
                                        <button id="<%=id+"-"+course.get("수업번호")%>" class="handbook-btn apply-btn"
                                                onclick="sendRequest(
                                                    '<%=course.get("class_id")%>',
                                                    '<%=course.get("수업번호")%>',
                                                    '<%=course.get("학수번호")%>',
                                                    '<%=course.get("교과목명")%>',
                                                    '<%=course.get("credit")%>',
                                                    '<%=course.get("교강사이름")%>',
                                                    '<%=course.get("수업시간")%>',
                                                    '<%=course.get("수강정원")%>',
                                                    '<%=course.get("강의실")%>'
                                                )">신청
                                        </button>
                                    </div>
                                    <div class="handbook-list-content-item w8"><%=course.get("수업번호")%></div>
                                    <div class="handbook-list-content-item w8"><%=course.get("학수번호")%></div>
                                    <div class="handbook-list-content-item w13"><%=course.get("교과목명")%></div>
                                    <div class="handbook-list-content-item w8"><%=course.get("교강사이름")%></div>
                                    <div class="handbook-list-content-item w23">
                                        <%=course.get("수업시간") != null ? course.get("수업시간") : ""%>
                                    </div>
                                    <div class="handbook-list-content-item w10"><%=course.get("수강정원")%></div>
                                    <div class="handbook-list-content-item w14"><%=course.get("강의실")%></div>
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
    <script>
        $.ajax({
            async: true, type: "GET",
            url: "http://localhost:8080/project1_war_exploded/enrolment/EnrolmentServlet",
            data: {"student_id": <%=id%>},
            success: (response) => {
                response.map((v,i) =>{
                    const element_id = <%=id%>+'-'+ v['class_no'];
                    if(document.getElementById(element_id) != null){
                        document.getElementById(element_id).setAttribute("disabled", "disabled");
                    }
                })
            },
            error: (response) => {console.log(response)}
        });
    </script>
    <script>
        function sendRequest(...data) {
            $.ajax({
                async: true, type: "POST",
                url: "http://localhost:8080/project1_war_exploded/enrolment/EnrolmentServlet",
                data: {
                    "student_id": <%=id%>,
                    "class_id": data[0],
                    "class_no": data[1],
                    "course_id": data[2],
                    "course_name": data[3],
                    "credit": data[4],
                    "lecturer_name": data[5],
                    "class_time": data[6],
                    "course_capacity": data[7],
                    "lecture_place": data[8]
                },
                success: (response) => {
                    alert(response);
                    $('.handbook-submit-btn').click();
                },
                error: (response) => console.log(response)
            });
        }
    </script>
</html>
