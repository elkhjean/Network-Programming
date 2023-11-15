package com.guessinggame.view;

public class gameView {

    public static String getGamePage() {
        String html = "<html>"
                + "<head><title>Number Guess Game </title>"
                + "<body>Welcome to the Number Guess Game. Guess a number between 1 and 100</body>"
                + "<form name=\"guessform\" method=\"post\">"
                + "<input type=text name=guess>"
                + "<input type=submit value=\"Guess\">"
                + "</form>"
                + "</html>";
        return html;
    }

        public static String getGamePage(String gameResponse) {
        String html = "<html>"
                + "<head><title>Number Guess Game </title>"
                + "<body>Welcome to the Number Guess Game. Guess a number between 1 and 100</body>"
                + "<form name=\"guessform\" method=\"post\">"
                + "<input type=text name=guess>"
                + "<input type=submit value=\"Guess\">"
                + "</form>"
                + "<body>" + gameResponse + "</body>"
                + "</html>";
        return html;
    }

}