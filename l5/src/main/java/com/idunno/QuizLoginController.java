package com.idunno;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
@RequestMapping({"/quizLogin", "/"})
@SessionAttributes("userId")
public class QuizLoginController {

    @GetMapping
    public String getLoginPage() {
        return "quizLoginView";
    }

    @PostMapping
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password,
            Model model) {
        String userId = null;
        try {
            userId = loadUserIdFromCredentials(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection problem.");
        }
        if (userId != null) {
            model.addAttribute("userId", userId);
            return "redirect:/chooseQuiz";
        }

        model.addAttribute("loginFailed", true);
        return "quizLoginView";

    }

    private String loadUserIdFromCredentials(String username, String password) throws SQLException {
        try (PreparedStatement pstmt = DatabaseUtility.getConnection()
                .prepareStatement("SELECT ID FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("ID");
                return null;
            }
        }
    }
}
