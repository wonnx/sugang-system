package handbook;

import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "HandbookServlet", value = "/handbook/HandbookServlet")
public class HandbookServlet extends HttpServlet {

    public HandbookServlet() {super();}

    public ArrayList<HashMap<String,Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        while(rs.next()) {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);

        String class_no = request.getParameter("class_no");
        String course_id = request.getParameter("course_id");
        String course_name = request.getParameter("course_name");

        Connection conn = null;
        String server = "localhost:3307"; // MySQL server port
        String database = "DB2017029470"; // MySQL database name
        String user_name = "root"; //  MySQL server id
        String password = "00000000"; // MySQL server password

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
            System.out.println("database connection success");
        } catch(SQLException e) {
            System.err.println("con error:" + e.getMessage());
            e.printStackTrace();
        }

        // 3. SQL implement
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql = "SELECT \n" +
                "\t수업번호, 학수번호, 교과목명, 교강사이름,\n" +
                "group_concat(수업시간 SEPARATOR ', ') AS 수업시간, 수강정원, 강의실\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\t\tT6.class_no AS 수업번호,\n" +
                "\t\tT6.course_id AS 학수번호,\n" +
                "\t\tT6.name AS 교과목명,\n" +
                "\t\tT6.lecturer_name AS 교강사이름,\n" +
                "\t\tCONCAT(\n" +
                "\t\t\tT6.enrollments,\n" +
                "\t\t\t' / ',\n" +
                "\t\t\tT6.person_max\n" +
                "\t\t) AS 수강정원,\n" +
                "\t\tCONCAT(\n" +
                "\t\t\tT6.class_day_of_week,\n" +
                "\t\t\t' (',\n" +
                "\t\t\tSUBSTR(T6.class_time, 12, 5),\n" +
                "\t\t\t' - ',\n" +
                "\t\t\tSUBSTR(T6.class_time, 34, 5),\n" +
                "\t\t\t')'\n" +
                "\t\t) AS 수업시간,\n" +
                "\t\tCONCAT(building.name, ' ', T6.room_id, '호') AS 강의실 \n" +
                "\tFROM (\n" +
                "\t\tSELECT T4.*, T5.enrollments\n" +
                "\t\tFROM (\n" +
                "\t\t\tSELECT \n" +
                "\t\t\t\tT3.*, room.building_id, WEEKDAY(T3.begin),\n" +
                "\t\t\t\tCASE WEEKDAY(T3.begin)\n" +
                "\t\t\t\t\tWHEN '0' THEN '월요일'\n" +
                "\t\t\t\t\tWHEN '1' THEN '화요일'\n" +
                "\t\t\t\t\tWHEN '2' THEN '수요일'\n" +
                "\t\t\t\t\tWHEN '3' THEN '목요일'\n" +
                "\t\t\t\t\tWHEN '4' THEN '금요일'\n" +
                "\t\t\t\t\tWHEN '5' THEN '토요일'\n" +
                "\t\t\t\t\tWHEN '6' THEN '일요일'\n" +
                "\t\t\t\tEND AS class_day_of_week,\n" +
                "\t\t\t\tCONCAT(\n" +
                "\t\t\t\t\tDATE_ADD(T3.begin, INTERVAL 12 HOUR),\n" +
                "\t\t\t\t\t' - ', \n" +
                "\t\t\t\t\tDATE_ADD(T3.end, INTERVAL 12 HOUR)\n" +
                "\t\t\t\t) AS class_time\n" +
                "\t\t\tFROM (\n" +
                "\t\t\t\tSELECT T2.*, time.begin, time.end AS end FROM (\n" +
                "\t\t\t\t\tSELECT T1.*, lecturer.name AS lecturer_name FROM (\n" +
                "\t\t\t\t\t\tSELECT class.*, course.name AS course_name\n" +
                "\t\t\t\t\t\tFROM class JOIN course\n" +
                "\t\t\t\t\t\tON class.course_id = course.course_id\n" +
                "\t\t\t\t\t\tAND class.class_no LIKE ? \n" +
                "\t\t\t\t\t\tAND course.course_id LIKE ? \n" +
                "\t\t\t\t\t\tAND course.name LIKE ? \n" +
                "\t\t\t\t\t) AS T1 JOIN lecturer\n" +
                "\t\t\t\t\tON T1.lecturer_id = lecturer.lecturer_id\n" +
                "\t\t\t\t) AS T2 JOIN time\n" +
                "\t\t\t\tON T2.class_id = time.class_id\n" +
                "\t\t\t) AS T3 JOIN room\n" +
                "\t\t\tON T3.room_id = room.room_id\n" +
                "\t\t) AS T4 JOIN (\n" +
                "\t\t\tSELECT course_id, credits.year, count(course_id) AS enrollments\n" +
                "\t\t\tFROM credits\n" +
                "\t\t\tGROUP BY course_id, credits.year\n" +
                "\t\t) AS T5\n" +
                "\t\tON (T4.course_id, T4.opened) = (T5.course_id, T5.year)\n" +
                "\t) AS T6 JOIN building\n" +
                "\tON T6.building_id = building.building_id\n" +
                ") AS T7\n" +
                "GROUP BY 수업번호, 학수번호, 교과목명, 교강사이름, 수강정원, 강의실;";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, class_no != "" ? class_no : "%");
            pstmt.setString(2, course_id != "" ? course_id : "%");
            pstmt.setString(3, course_name != "" ? "%"+course_name+"%" : "%");
            rs = pstmt.executeQuery();

            session.setAttribute("class_no", class_no);
            session.setAttribute("course_id", course_id);
            session.setAttribute("course_name", course_name);
            ArrayList<HashMap<String, Object>> course_list = convertResultSetToArrayList(rs);
            session.setAttribute("handbook", course_list);
            response.sendRedirect("./handbook.jsp");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }

        // 4. terminate the connection
        if (rs != null) {
            try {
                rs.close();
            } catch(SQLException e) {
                System.out.println(e);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch(SQLException e) {
                System.out.println(e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch(SQLException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("handbook servlet do post function");
    }
}
