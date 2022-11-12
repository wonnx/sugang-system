package login;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "LoginServlet", value = "/login/LoginServlet")
public class LoginServlet extends HttpServlet {
    public LoginServlet() {
        super();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Login Servlet doGet function");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession(true);

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String md = request.getParameter("md");
        int mode = Integer.parseInt(md);

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
        String sql = null;
        if(mode == 0){
            sql = "select * from manager where manager_id = ? and password = ?";
        }else{
            sql = "select * from student where student_id = ? and password = ?";
        }
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);

            rs = pstmt.executeQuery();

            if(rs.next()){
                String name = rs.getString("name");
                session.setAttribute("id", id);
                session.setAttribute("name", name);
                response.sendRedirect("./loginSuccess.jsp");
            }else{
                PrintWriter writer = response.getWriter();
                writer.println("<script>");
                writer.println("alert('현재 입력하신 아이디가 등록되어 있지 않거나, 아이디 또는 비밀번호를 잘못 입력 하셨습니다.');");
                if(mode == 0){
                    writer.println("location.href='./adminLogin.jsp';");
                }else{
                    writer.println("location.href='./userLogin.jsp';");
                }
                writer.println("</script>");
            }
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
}
