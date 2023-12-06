package com.idunno;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    private Integer id;
    private Integer userId;
    private Integer quizId;
    private Integer score;

    public Result(Integer userId, Integer quizId, Integer score) {
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
    }

    protected Result() {
        // For JPA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return this.id;
    }

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    @Column(name = "quiz_id")
    public Integer getQuizId() {
        return quizId;
    }

    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
