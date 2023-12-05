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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class QuizzLoginController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");

        HttpSession session = request.getSession(true);
        RequestDispatcher rd = request.getRequestDispatcher("quizzLoginView.jsp");
        rd.forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        out.println("Connecting to DB.");
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/derby");
            Connection conn = ds.getConnection();

            PreparedStatement pstmt = conn.prepareStatement("SELECT ID FROM users WHERE username = ? AND password = ?");
            pstmt.setString(1, request.getParameter("username"));
            pstmt.setString(2, request.getParameter("password"));
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                request.setAttribute("loginFailed", true);
                RequestDispatcher rd = request.getRequestDispatcher("quizzLoginView.jsp");
                rd.forward(request, response);
            } else {
                session.setAttribute("model", new QuizzModel(rs.getString("ID")));
                RequestDispatcher rd = request.getRequestDispatcher("/ChooseQuizzController");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Database connection problem.");
        }

    }
}
