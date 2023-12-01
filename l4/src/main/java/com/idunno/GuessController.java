package com.idunno;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GuessController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        if (session.getAttribute("model") == null) {
            session.setAttribute("model", new GuessModel());
        }

        RequestDispatcher rd = request.getRequestDispatcher("guessView.jsp");
        rd.forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        GuessModel m = (GuessModel) session.getAttribute("model");
        m.compareGuess(request.getParameter("guess"));

        RequestDispatcher rd = request.getRequestDispatcher("guessView.jsp");
        rd.forward(request, response);

    }
}