package test;

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
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    // Första anrop blir med GET så det hanterar vi här genom att tilldela ett sessionsobjekt
    HttpSession session = request.getSession(true); // kan användas för att förhindra fler klienter.
    if(session.getAttribute("model")==null){
        session.setAttribute("model", new Model());
    }
    
    // Vi vill inte använda redirect för det skapar en extra request från klienten (HTTP 300-meddelande om redirect)
    RequestDispatcher rd = request.getRequestDispatcher("index.html");
    rd.forward(request, response);

}  


public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    // Här hamnar vi fr o m request två (som kommer från index.html)
    HttpSession session = request.getSession();
    Model m = (Model)session.getAttribute("model");
     m.setUsername(request.getParameter("username")); // hämtar värdet från formuläret i index.html och lägger det i modellen.
     
    // Vill man associera något till applicationen som helhet (något alla klienter delar på) kan man använda "application-scope"
    ServletContext application = request.getServletContext();
    // Tilldelar något godtyckligt här för att visa att det är samma metoder för get/set i application som i session.
    application.setAttribute("slask", "tramsvärde");
  
   

    RequestDispatcher rd = request.getRequestDispatcher("view.jsp");
    rd.forward(request, response);

}
}