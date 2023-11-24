<!-- Direktivet page kan användas för att vid behov göra import eller inkludera en annan sida -->
   
<%@page contentType="text/html" pageEncoding="UTF-8"%>


    
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Välkommen</title>
    </head>
    <body>
       
<%
    // I en .jsp-sida (som transkriberas till en servlet finns många färdigdefinierade variabler
    // out: En PrintWriter
    // session: Samma som session i servlet:en
    // application: Samma som application i servlet:en
    // request: Samma som request i servlet:en
    // response: Samma som response i servlet:en
    // OBS: Det är inte namnvalen i servlet:en som styr namnen här, de har alltid dessa namn i skriplets (= java-taggar i jsp-sidor)
    //I övrigt kan vilken javakod som helst användas och fördefinierade variabler existerar
    test.Model m = (test.Model)session.getAttribute("model");
    out.println("Välkommen: " + m.getUsername());
%>
        
    </body>
</html>
