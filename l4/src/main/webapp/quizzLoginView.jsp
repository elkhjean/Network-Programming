<!-- Direktivet page kan användas för att vid behov göra import eller inkludera en annan sida -->

<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <title>Number Guess Game</title>
    </head>

    <body>
        Hello, please log in with your email and password
        <form name="username" method="post">
            <input type="text" name="username">
            <input type="text" name="password">
            <input type="submit" value="loginCredentials">
        </form>
        <% 
        com.idunno.GuessModel m=(com.idunno.GuessModel)session.getAttribute("model"); 
        out.println(m.getMessage());
        %>
   
    </body>
</html>