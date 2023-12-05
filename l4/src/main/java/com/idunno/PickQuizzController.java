package com.idunno;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PickQuizzController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ArrayList<String> quizzes = new ArrayList<>();

        ArrayList<String> quizzesIDs = new ArrayList<>();

        HttpSession session = request.getSession();
        QuizzModel m = (QuizzModel) session.getAttribute("model");
        // if (m.getUserId() == null) {
        // out.println("usr not found");
        // RequestDispatcher rd = request.getRequestDispatcher("/QuizzLoginController");
        // rd.forward(request, response);

        // }

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/derby");
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from quizzes");

            while (rs.next()) {
                quizzesIDs.add(rs.getString("id"));
                quizzes.add(rs.getString("subject"));

            }
            request.setAttribute("subjects", quizzes);
            request.setAttribute("ids", quizzesIDs);
            RequestDispatcher rd = request.getRequestDispatcher("pickQuizz.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        QuizzModel m = (QuizzModel) session.getAttribute("model");
        m.setQuizzId(request.getParameter("quizId"));

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/derby");
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            String quString = m.getQuizzId();
            ResultSet rs = stmt.executeQuery("select " + quString + " from selector");
            while (rs.next()) {
                m.addQuestionId(rs.getString("question_id"));
            }
            RequestDispatcher rd = request.getRequestDispatcher("/quizz");
            rd.forward(request, response);

        } catch (Exception e) {
            //
        }
    }
}
