package enrolment;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;

@WebServlet(name = "EnrolmentServlet", value = "/enrolment/EnrolmentServlet")
public class EnrolmentServlet extends HttpServlet {
    private String server = "localhost:3307"; // MySQL server port
    private String database = "DB2017029470"; // MySQL database name
    private String user_name = "root"; //  MySQL server id
    private String password = "00000000"; // MySQL server password
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String student_id = request.getParameter("student_id");

        Connection conn = null;

        // 1. driver loading
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("driver loading error: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. create connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?serverTimezone=UTC&useSSL=false", user_name, password);
            System.out.println("enrolment-servlet-doget: database connection success");
        } catch(SQLException e) {
            System.err.println("con error:" + e.getMessage());
            e.printStackTrace();
        }

        // 3. SQL implement
        ResultSet rs;
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM sugang WHERE student_id = ?;";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student_id);
            rs = pstmt.executeQuery();
            JSONArray json = new JSONArray();
            while(rs.next()){
                JSONObject j = new JSONObject();
                j.put("class_no", rs.getString("class_no"));
                json.put(j);
            }
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(json);
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String student_id = request.getParameter("student_id");
        String class_id = request.getParameter("class_id");
        String class_no = request.getParameter("class_no");
        String course_id = request.getParameter("course_id");
        String course_name = request.getParameter("course_name");
        String credit = request.getParameter("credit");
        String lecturer_name = request.getParameter("lecturer_name");
        String class_time = request.getParameter("class_time");
        String course_capacity = request.getParameter("course_capacity");
        String lecture_place = request.getParameter("lecture_place");

        Connection conn = null;

        // 1. driver loading
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("driver loading error: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. create connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?serverTimezone=UTC&useSSL=false", user_name, password);
            System.out.println("enrolment-servlet-dopost: database connection success");
        } catch(SQLException e) {
            System.err.println("con error:" + e.getMessage());
            e.printStackTrace();
        }

        // 3. SQL implement
        int result = 0;
        String grade = null;
        ResultSet rs_1 = null;
        PreparedStatement pstmt_1 = null;
        String sql_1 = "SELECT * FROM credits WHERE student_id = ? AND course_id = ?;";
        try{
            pstmt_1 = conn.prepareStatement(sql_1);
            pstmt_1.setString(1, student_id);
            pstmt_1.setString(2, course_id);
            rs_1 = pstmt_1.executeQuery();
            if(rs_1.next()){
                grade = rs_1.getString("grade");
                if(grade.charAt(0) - 'B' >= 1){
                    result = -1;
                }else{
                    result = -2;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        ResultSet rs_2 = null;
        PreparedStatement pstmt_2 = null;
        String sql_2 = "SELECT * FROM sugang WHERE student_id = ? AND course_id = ?;";
        try{
            pstmt_2 = conn.prepareStatement(sql_2);
            pstmt_2.setString(1, student_id);
            pstmt_2.setString(2, course_id);
            rs_2 = pstmt_2.executeQuery();
            if(rs_2.next()){
                result = -3;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }

        int count = 0;
        ResultSet rs_3 = null;
        PreparedStatement pstmt_3 = null;
        String sql_3 =
                "SELECT DISTINCT class_no, credit\n" +
                "FROM (\n" +
                "\tSELECT T1.*\n" +
                "\tFROM sugang AS T1\n" +
                "\tJOIN class AS T2\n" +
                "\tON T1.class_no = T2.class_no\n" +
                "\tWHERE student_id LIKE ? \n" +
                ") AS T3;";
        try{
            pstmt_3 = conn.prepareStatement(sql_3);
            pstmt_3.setString(1, student_id);
            rs_3 = pstmt_3.executeQuery();
            while(rs_3.next()){
                int c = Integer.parseInt(rs_3.getString("credit"));
                count = count + c;
            }

            if(count + Integer.parseInt(credit) >= 20){
                result = -4;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }

        String[] t = course_capacity.split(" / ");
        int sugang = Integer.parseInt(t[0]);
        int person = Integer.parseInt(t[1]);
        if(sugang >= person){
            result = -5;
        }

        String s0 = class_time;
        s0 = s0.replaceAll("월요일", "1900-01-01");
        s0 = s0.replaceAll("화요일", "1900-01-02");
        s0 = s0.replaceAll("수요일", "1900-01-03");
        s0 = s0.replaceAll("목요일", "1900-01-04");
        s0 = s0.replaceAll("금요일", "1900-01-05");
        s0 = s0.replaceAll("토요일", "1900-01-06");
        s0 = s0.replaceAll("일요일", "1900-01-07");
        s0 = s0.replace("(", "");
        s0 = s0.replace(")", "");

        String[] s1 = null;
        String[] s2 = null;
        String[] s3 = null;
        if(!s0.equals("null")){
            s1 = s0.split(", ");
            if(s1[0] != s0){
                s2 = s1[0].split(" - ");
                s2[0] = s2[0] + ":00";
                s2[1] = s2[0].substring(0, 11) + s2[1] + ":00";
                s3 = s1[1].split(" - ");
                s3[0] = s3[0] + ":00";
                s3[1] = s3[0].substring(0, 11) + s3[1] + ":00";
            }else{
                s2 = s0.split(" - ");
                s2[0] = s2[0] + ":00";
                s2[1] = s2[0].substring(0, 11) + s2[1] + ":00";
            }
        }

        ResultSet rs_4_1 = null;
        ResultSet rs_4_2 = null;
        PreparedStatement pstmt_4 = null;
        String sql_4 =
                "SELECT *\n" +
                        "FROM (\n" +
                        "\tSELECT\n" +
                        "\t\tstudent_id, T2.class_id, T2.class_no,\n" +
                        "\t\ttime_id, period, begin, end\n" +
                        "\tFROM (\n" +
                        "\t\tSELECT \n" +
                        "\t\t\ttime_id, class_id, period,\n" +
                        "\t\t\tdate_add(date_format(begin, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 HOUR) AS begin,\n" +
                        "\t\t\tdate_add(date_format(end, '%Y-%m-%d %H:%i:%s'), INTERVAL 12 HOUR) AS end\n" +
                        "\t\tFROM time\n" +
                        "\t) AS T1 JOIN sugang AS T2\n" +
                        "\tON T1.class_id = T2.class_id AND T2.student_id LIKE ? \n" +
                        ") AS T3\n" +
                        "WHERE \n" +
                        "\t(begin > ? AND begin < ?) OR\n" +
                        "    (begin = ? AND begin < ?) OR\n" +
                        "    (begin < ? AND end > ? AND ? > ?);";

        boolean flag = true;
        try{
            pstmt_4 = conn.prepareStatement(sql_4);
            if(s2 != null){
                pstmt_4.setString(1, student_id);
                pstmt_4.setString(2, s2[0]);
                pstmt_4.setString(3, s2[1]);
                pstmt_4.setString(4, s2[0]);
                pstmt_4.setString(5, s2[1]);
                pstmt_4.setString(6, s2[0]);
                pstmt_4.setString(7, s2[0]);
                pstmt_4.setString(8, s2[1]);
                pstmt_4.setString(9, s2[0]);
                rs_4_1 = pstmt_4.executeQuery();
                if(rs_4_1.next())flag = false;
            }
            if(s3 != null){
                pstmt_4.setString(1, s3[0]);
                pstmt_4.setString(2, s3[1]);
                pstmt_4.setString(3, s3[0]);
                pstmt_4.setString(4, s3[1]);
                pstmt_4.setString(5, s3[0]);
                pstmt_4.setString(6, s3[0]);
                pstmt_4.setString(7, s3[1]);
                pstmt_4.setString(8, s3[0]);
                rs_4_2 = pstmt_4.executeQuery();
                if(rs_4_2.next())flag = false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e);
        }
        if(!flag) result = -6;

        PreparedStatement pstmt_5 = null;
        String sql_5 = "INSERT INTO sugang VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        if(result == 0 || result == -1){
            try{
                pstmt_5 = conn.prepareStatement(sql_5);
                pstmt_5.setString(1, student_id);
                pstmt_5.setString(2, class_id);
                pstmt_5.setString(3, class_no);
                pstmt_5.setString(4, course_id);
                pstmt_5.setString(5, course_name);
                pstmt_5.setString(6, credit);
                pstmt_5.setString(7, lecturer_name);
                pstmt_5.setString(8, class_time);
                pstmt_5.setString(9, course_capacity);
                pstmt_5.setString(10, lecture_place);
                try{
                    int res = pstmt_5.executeUpdate();
                    if(result == 0) result = res;
                }catch(SQLIntegrityConstraintViolationException e){
                    result = -7;
                }
            }catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }

        // 기존에 수강 신청한 과목과 시간이 겹치는 경우


        response.setContentType("text/plain");
        if(result >= 1){ // 수강 신청이 성공적으로 완료된 경우
            response.getWriter().write("수강신청이 완료되었습니다.");
        }else if(result == 0){ // 데이터베이스나 서버 오류로 인하여 수강신청에 실패한 경우
            response.getWriter().write("내부 서버 오류로 인해, 수강신청에 실패했습니다.");
        }else if(result == -1){ // 기존에 수강한 기록이 있지만, 재수강이 가능한 경우
            response.getWriter().write("기존 성적 ("+ grade +")\n성적 향상을 위한 재수강 신청이 완료되었습니다.");
        }else if(result == -2){ // 현재 수강 신청한 과목을 이전에 수강한 기록이 있는 경우
            response.getWriter().write("과거에 해당 과목을 수강한 기록이 있습니다.\n 다른 과목을 다시 선택해 주세요.");
        }else if(result == -3){ // 현재 수강 신청한 과목과 학수번호가 같은 과목을 신청한 경우
            response.getWriter().write("동일한 과목을 수강신청 하였습니다. 다른 과목을 다시 선택해 주세요.");
        }else if(result == -4){ // 수강할 수 있는 최대 학점을 넘은 경우
            response.getWriter().write("수강 최대 학점을 초과하였습니다.");
        }else if(result == -5){ // 해당 과목을 수강 최대 인원이 수강신청한 경우
            response.getWriter().write("수강 정원을 초과하였습니다. 다른 과목을 선택해주세요.");
        }else if(result == -6){ // 동일한 시간대에 다른 과목을 수강신청한 경우
            response.getWriter().write("동일한 시간대에 다른 과목을 수강신청 하셨습니다. 다른 과목을 선택해주세요.");
        } else if(result == -7){ // 수강 신청 내역에 학수번호가 동일한 과목이 존재하는 경우
            response.getWriter().write("이미 수강신청이 완료된 과목입니다.");
        }

        // 5. terminate the connection
        if (conn != null) {
            try {
                conn.close();
            } catch(SQLException e) {
                System.out.println(e);
            }
        }
    }
}