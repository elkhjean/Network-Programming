package com.idunno;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controller extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(true); // kan användas för att förhindra fler klienter.
        if (session.getAttribute("model") == null) {
            session.setAttribute("model", new Model());
        }

        RequestDispatcher rd = request.getRequestDispatcher("view.jsp");
        rd.forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("model") == null) {
            session.setAttribute("model", new Model());
        }

        Model m = (Model) session.getAttribute("model");
        m.compareGuess(request.getParameter("guess"));

        RequestDispatcher rd = request.getRequestDispatcher("view.jsp");
        rd.forward(request, response);

    }
}