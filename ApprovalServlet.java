import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/ApprovalServlet")
public class ApprovalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("requestId");
        String action = request.getParameter("action"); // "Approve" or "Reject"
        String status = action.equals("Approve") ? "Approved" : "Rejected";

        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/yourdb", "youruser", "yourpassword");
            String sql = "UPDATE requests SET status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, requestId);
            pstmt.executeUpdate();
            conn.close();
            response.sendRedirect("pendingRequests.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
