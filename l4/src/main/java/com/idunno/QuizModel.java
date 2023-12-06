package com.idunno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class QuizModel {
    private String userId;
    private String quizId;
    private ArrayList<String> questionIds;
    private Question currentQuestion;
    private int score;

    public String getScore() {
        return Integer.toString(this.score);
    }

    public void addToScore() {
        this.score++;
    }

    public QuizModel(String userId, String quizId) {
        this.questionIds = new ArrayList<>();
        this.userId = userId;
        this.quizId = quizId;
        this.score = 0;
    }

    public String pickRandomQuestionId() {
        Random ran = new Random();
        int size = questionIds.size(), i;

        if (size <= 1)
            i = 0;
        else
            i = ran.nextInt(size);
        String qId = questionIds.get(i);
        questionIds.remove(i);
        return qId;
    }

    public boolean isQuizCompleted() {
        return questionIds.isEmpty();
    }

    public void addQuestionId(String id) {
        this.questionIds.add(id);
    }

    public boolean checkGuess(String guess) {
        if (currentQuestion.answer.equals(guess)) {
            return true;
        }
        return false;
    }

    public void setCurrentQuestion(String options, String answer, String questionText, String questionId) {
        this.currentQuestion = new Question(options, answer, questionText, questionId);
    }

    public void updateScoreToDb() throws SQLException {
        try (Connection conn = DatabaseUtility.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE results SET score = ? WHERE user_id = ? AND quiz_id = ?")) {
            stmt.setString(1, getScore());
            stmt.setString(2, getUserId());
            stmt.setString(3, getQuizId());
            stmt.executeUpdate();

        }
    }

    private class Question {
        private String[] options;
        private String answer;
        private String questionText;
        private String questionId;

        public Question(String options, String answer, String questionText, String questionId) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.answer = answer.replace("/", "");
            this.options = options.split("/");
        }
    }

    public String[] getOptions() {
        return this.currentQuestion.options;
    }

    public String getAnswer() {
        return this.currentQuestion.answer;
    }

    public String getQuestionText() {
        return this.currentQuestion.questionText;
    }

    public String getQuestionId() {
        return this.currentQuestion.questionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getQuizId() {
        return quizId;
    }

    public ArrayList<String> getQuestionIds() {
        return questionIds;
    }
}