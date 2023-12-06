<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Options Form</title>
    </head>

    <body>
        <% if(request.getAttribute("msg") == null){ %>
            <%= request.getAttribute("text") %>
            <form name="quizForm" method="post">
                <% String[] options=(String[]) request.getAttribute("options"); 
                for(int i=0; i < options.length; i++) {
                    %>
                    <input type="checkbox" name="<%= options[i] %>" value="1">
                    <%= options[i] %>
                        <br>
                        <% } %>
                            <input type="submit" value="Submit">
            <% } else { %>
                <%= request.getAttribute("msg")%>
                <form action="quiz" method="GET">
                    <input type="submit" value="Next Question">
                </form>
                <% } %>

    </body>

    </html>