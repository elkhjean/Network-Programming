<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.idunno.QuizzItem" %>


<!DOCTYPE html>
<html>

<head>
    <title>Quiz game</title>
</head>

<body>
    Welcome to choose between these subjects.

    <%
        ArrayList<QuizzItem> quizzItems = (ArrayList<QuizzItem>) request.getAttribute("quizzItems");
        for (int i = 0; i < quizzItems.size(); i++) {
            String subject = quizzItems.get(i).getSubject();
            String id = quizzItems.get(i).getId();
            String score = quizzItems.get(i).getScore();
        
            %>
            <form name="guessform_<%= i %>" method="post">
                <input type="hidden" name="quizId" value="<%= id %>">
                <input type="submit" value="<%= subject %>">
                <span> Score: <%= score %> </span> <!-- Display score here -->
            </form>
            <br>
    <% 
        }
    %>

</body>

</html>
