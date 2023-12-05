<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>

<head>
    <title>Quiz game</title>
</head>

<body>
    Welcome to choose between these subjects.

    <%
        ArrayList<String> subjects = (ArrayList<String>) request.getAttribute("subjects");
        ArrayList<String> ids = (ArrayList<String>) request.getAttribute("ids");
        for (int i = 0; i < subjects.size(); i++) {
            String subject = subjects.get(i);
            String id = ids.get(i);
    %>
            <form name="guessform_<%= i %>" method="post">
                <input type="hidden" name="quizId" value="<%= id %>">
                <input type="submit" value="<%= subject %>">
            </form>
            <br>
    <% 
        }
    %>

</body>

</html>
