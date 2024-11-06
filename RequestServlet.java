import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String softwareId = request.getParameter("softwareId");
        String accessType = request.getParameter("accessType");
        String reason = request.getParameter("reason");

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/yourdb", "youruser", "yourpassword");
            String sql = "INSERT INTO requests (user_id, software_id, access_type, reason, status) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?, ?, 'Pending')";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, softwareId);
            pstmt.setString(3, accessType);
            pstmt.setString(4, reason);
            pstmt.executeUpdate();
            conn.close();
            response.sendRedirect("requestAccess.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
