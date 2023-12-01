package com.idunno;

import java.util.ArrayList;
import java.util.Random;

public class QuizzModel {
    private String userId;
    private String quizzId;
    private ArrayList<Integer> questionIds;
    private Question currentQuestion;

    public QuizzModel(String userId) {
        this.userId = userId;
    }

    public int pickRandomQuestionId() {
        Random ran = new Random();
        int i = ran.nextInt(questionIds.size());
        int qId = questionIds.get(i);
        questionIds.remove(i);
        return qId;
    }

    public void setQuizzId(String quizId) {
        this.quizzId = quizId;
    }

    public boolean checkGuess(String guess) {
        if (currentQuestion.equals(guess)) {
            return true;
        }
        return false;
    }

    public Question getQuestion() {
        return this.currentQuestion;
    }

    class Question {
        private String[] options;
        private String answer;
        private String questionText;
        private int questionId;

        public Question(String options, String answer, String questionText, String questionId) {
            this.questionId = Integer.parseInt(questionId);
            this.questionText = questionText;
            this.answer = answer.replace("/", "");
            this.options = options.split("/");
        }

        public String[] getOptions() {
            return options;
        }

        public String getAnswer() {
            return answer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public int getQuestionId() {
            return questionId;
        }

    }
}