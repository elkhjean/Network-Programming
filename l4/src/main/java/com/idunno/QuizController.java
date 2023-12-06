package com.idunno;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class QuizController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        QuizModel model = (QuizModel) session.getAttribute("quizModel");

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("quizLogin");
            return;
        }
        if (model.isQuizCompleted()) {
            try {
                 //model.updateScoreToDb();
                model.updateWithJPA();
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            response.sendRedirect("chooseQuiz");
            return;
        }

        try (Connection conn = DatabaseUtility.getConnection()) {
            loadRandomQuestionFromDB(model, conn);
            request.setAttribute("options", model.getOptions());
            request.setAttribute("text", model.getQuestionText());
            RequestDispatcher rd = request.getRequestDispatcher("quizGameView.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        QuizModel model = (QuizModel) session.getAttribute("quizModel");

        String answer = getAnswerString(request, model);
        if (model.checkGuess(answer)) {
            model.addToScore();
            request.setAttribute("msg", "Correct!");
        } else
            request.setAttribute("msg", "Wrong!");
        RequestDispatcher rd = request.getRequestDispatcher("quizGameView.jsp");
        rd.forward(request, response);
    }

    private String getAnswerString(HttpServletRequest request, QuizModel model) {
        StringBuilder sb = new StringBuilder();
        for (String option : model.getOptions()) {
            if (request.getParameter(option) == null)
                sb.append("0");
            else
                sb.append("1");
        }
        return sb.toString();
    }

    private void loadRandomQuestionFromDB(QuizModel model, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE id = ?")) {
            String questionId = model.pickRandomQuestionId();
            stmt.setString(1, questionId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            model.setCurrentQuestion(rs.getString("options"), rs.getString("answer"), rs.getString("text"),
                    questionId);
        }
    }
}