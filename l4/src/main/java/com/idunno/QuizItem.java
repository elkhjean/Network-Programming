package com.idunno;

public class QuizItem {
    String id;
    private String subject;
    private String score;

    public QuizItem(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public void addScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getScore() {
        return score;
    }
}