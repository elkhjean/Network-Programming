package com.idunno;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/chooseQuiz")
@SessionAttributes("quizModel")
public class PickQuizController {

    @GetMapping
    public String getQuizzes(Model model, @SessionAttribute("userId") String userId) {
        if (userId == null) {
            return "redirect:/quizLogin";
        }

        try (Connection conn = DatabaseUtility.getConnection()) {
            ArrayList<QuizItem> qItems = new ArrayList<>();
            loadQuizzesFromDb(conn, qItems);
            loadLatestUserScoresFromDb(userId, conn, qItems);
            model.addAttribute("quizItems", qItems);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "pickQuiz";
    }

    @PostMapping
    public String chooseQuiz(@SessionAttribute("userId") String userId, @RequestParam("quizId") String quizId,
            Model model) {
        if (userId == null) {
            return "redirect:/quizLogin";
        }

        QuizModel qModel = new QuizModel(userId, quizId);

        try (Connection conn = DatabaseUtility.getConnection()) {
            loadQuestionIdsFromDbIntoModel(qModel, quizId, conn);
        } catch (Exception e) {
            return "error" + e.getStackTrace(); // TODO Handle the error
        }

        model.addAttribute("quizModel", qModel);
        return "redirect:/quiz"; // Redirect to the quiz page
    }

    private void loadQuestionIdsFromDbIntoModel(QuizModel qModel, String quizId, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM selector WHERE quiz_id = ?")) {
            stmt.setString(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                qModel.addQuestionId(rs.getString("question_id"));
        }
    }

    private void loadQuizzesFromDb(Connection conn, ArrayList<QuizItem> qItems) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quizzes");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                qItems.add(new QuizItem(rs.getString("id"), rs.getString("subject")));
            }
        }
    }

    private void loadLatestUserScoresFromDb(String userId, Connection conn, ArrayList<QuizItem> qItems)
            throws SQLException {
        for (QuizItem item : qItems) {
            try (PreparedStatement stmt = conn
                    .prepareStatement(
                            "SELECT score FROM results WHERE user_id = ? AND quiz_id = ? ORDER BY id DESC FETCH FIRST ROW ONLY")) {
                stmt.setString(1, userId);
                stmt.setString(2, item.getId());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                item.addScore((rs.getString("score")));
            }
        }
    }
}
