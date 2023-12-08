<!-- Direktivet page kan användas för att vid behov göra import eller inkludera en annan sida -->

<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <title>Number Guess Game</title>
    </head>

    <body>
        Hello, please log in with your email and password
        <form name="loginForm" method="post">
            <label for="username">Username:</label>
            <input type="text" name="username" id="username"><br>

            <label for="password">Password:</label>
            <input type="password" name="password" id="password"><br>

            <input type="submit" name="login" value="Login">
        </form>

    </body>

    </html>