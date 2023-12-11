<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.idunno.QuizItem" %>


<!DOCTYPE html>
<html>

<head>
    <title>Quiz game</title>
</head>

<body>
    Welcome to choose between these subjects.

    <%
        ArrayList<QuizItem> quizItems = (ArrayList<QuizItem>) request.getAttribute("quizItems");
        for (int i = 0; i < quizItems.size(); i++) {
            String subject = quizItems.get(i).getSubject();
            String id = quizItems.get(i).getId();
            String score = quizItems.get(i).getScore();
            if(score == null)
                score = "";
        
            %>
            <form name="guessform_<%= i %>" method="post">
                <input type="hidden" name="quizId" value="<%= id %>">
                <input type="submit" value="<%= subject %>">
                <span> Score: <%= score %> </span>
            </form>
            <br>
    <% 
        }
    %>

</body>

</html>
