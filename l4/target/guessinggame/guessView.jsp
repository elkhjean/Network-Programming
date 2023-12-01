<!-- Direktivet page kan användas för att vid behov göra import eller inkludera en annan sida -->

<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <title>Number Guess Game</title>
    </head>

    <body>
        Welcome to the Number Guess Game. Guess a number between 1 and 100
        <form name="guessform" method="post">
            <input type="text" name="guess">
            <input type="submit" value="Guess">
        </form>
        <% 
        com.idunno.Model m=(com.idunno.Model)session.getAttribute("model"); 
        out.println(m.getMessage());
        %>
   
    </body>
</html>