
public class gameView {

    public static String getGamePage() {
        return getGamePage("");
    }

        public static String getGamePage(String gameResponse) {
            String html = "<html>\n"
                    + "<head><title>Number Guess Game </title>\n"
                    + "<body>Welcome to the Number Guess Game. Guess a number between 1 and 100</body>\n"
                    + "<form name=\"guessform\" method=\"post\">\n"
                    + "<input type=text name=guess>\n"
                    + "<input type=submit value=\"Guess\">\n"
                    + "</form>\n"
                    + "<body>" + gameResponse + "</body>\n"
                    + "</html>\n";
            return html;
        }

}