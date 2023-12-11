package com.idunno;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/quiz")

public class QuizController extends HttpServlet {

    @GetMapping
    public String getGame(@SessionAttribute("userId") String userId, @SessionAttribute("quizModel") QuizModel qModel,
            Model model) throws IOException, ServletException {

        if (userId == null) {
            return "redirect:/quizLogin";
        }
        if (qModel.isQuizCompleted()) {
            try {
                // qModel.updateScoreToDb();
                qModel.updateWithJPA();
            } catch (Exception e) {
                return "error" + e.getMessage();
            }
            return ("redirect:/chooseQuiz");
        }

        try (Connection conn = DatabaseUtility.getConnection()) {
            loadRandomQuestionFromDB(qModel, conn);
            model.addAttribute("options", qModel.getOptions());
            model.addAttribute("text", qModel.getQuestionText());
            return "quizGameView";
        } catch (Exception e) {
            return "error" + e.getMessage();
        }
    }

    @PostMapping
    public String postAnswer(Model model, @SessionAttribute("quizModel") QuizModel qModel,
            @RequestParam Map<String, String> answersMap) throws IOException, ServletException {

        String answer = getAnswerString(answersMap, qModel);
        if (qModel.checkGuess(answer)) {
            qModel.addToScore();
            model.addAttribute("msg", "Correct!");
        } else
            model.addAttribute("msg", "Wrong!");
        return "quizGameView";
    }

    private String getAnswerString(Map<String, String> answersMap, QuizModel qModel) {
        StringBuilder sb = new StringBuilder();
        for (String option : qModel.getOptions()) {
            if (!answersMap.containsKey(option))
                sb.append("0");
            else
                sb.append("1");
        }
        return sb.toString();
    }

    private void loadRandomQuestionFromDB(QuizModel model, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE id = ?")) {
            String questionId = model.pickRandomQuestionId();
            stmt.setString(1, questionId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            model.setCurrentQuestion(rs.getString("options"), rs.getString("answer"), rs.getString("text"),
                    questionId);
        }
    }
}