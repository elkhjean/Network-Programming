package com.idunno;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizLoginController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        RequestDispatcher rd = request.getRequestDispatcher("quizLoginView.jsp");
        rd.forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try (Connection conn = DatabaseUtility.getConnection()) {

            String userId = loadUserIdFromCredentials(request, conn);
            if (userId != null) {
                session.setAttribute("userId", userId);
                response.sendRedirect("chooseQuiz");
            } else {
                request.setAttribute("loginFailed", true);
                RequestDispatcher rd = request.getRequestDispatcher("quizLoginView.jsp");
                rd.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Database connection problem.");
        }

    }

    private String loadUserIdFromCredentials(HttpServletRequest request, Connection conn) throws SQLException {
        try (PreparedStatement pstmt = conn
                .prepareStatement("SELECT ID FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, request.getParameter("username"));
            pstmt.setString(2, request.getParameter("password"));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("ID");
                return null;
            }
        }
    }
}
