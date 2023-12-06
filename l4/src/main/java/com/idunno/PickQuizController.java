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
import java.util.ArrayList;

public class PickQuizController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("quizLogin");
            return;
        }

        try (Connection conn = DatabaseUtility.getConnection()) {
            ArrayList<QuizItem> qItems = new ArrayList<>();

            loadQuizzesFromDb(conn, qItems);
            loadUserScoresFromDb((String) session.getAttribute("userId"), conn, qItems);

            request.setAttribute("quizItems", qItems);
            RequestDispatcher rd = request.getRequestDispatcher("pickQuiz.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            out.println(e.getMessage());
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("quizLogin");
            return;
        }
        String quizId = request.getParameter("quizId");
        QuizModel model = new QuizModel(userId, quizId);
        session.setAttribute("quizModel", model);

        try (Connection conn = DatabaseUtility.getConnection()) {
            loadQuestionIdsFromDbIntoModel(model, quizId, conn);
            response.sendRedirect("quiz");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void loadQuestionIdsFromDbIntoModel(QuizModel model, String quizId, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM selector WHERE quiz_id = ?")) {
            stmt.setString(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                model.addQuestionId(rs.getString("question_id"));
        }
    }

    private void loadQuizzesFromDb(Connection conn, ArrayList<QuizItem> qItems) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quizzes");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                qItems.add(new QuizItem(rs.getString("id"), rs.getString("subject")));
            }
        }
    }

    private void loadUserScoresFromDb(String userId, Connection conn, ArrayList<QuizItem> qItems)
            throws SQLException {
        for (QuizItem item : qItems) {
            try (PreparedStatement stmt = conn
                    .prepareStatement("SELECT score FROM results WHERE user_id = ? AND quiz_id = ?")) {
                stmt.setString(1, userId);
                stmt.setString(2, item.getId());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                item.setScore((rs.getString("score")));
            }
        }
    }
}
