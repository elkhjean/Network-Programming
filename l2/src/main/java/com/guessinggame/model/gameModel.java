package com.guessinggame.model;

import java.util.Random;

public class gameModel {
    private String sessionId;
    private int numberOfGuesses;
    private int secretNumber;
    private boolean winStatus;

    public gameModel(String sessionId) {
        this.sessionId = sessionId;
        this.numberOfGuesses = 0;
        this.secretNumber = generateRandomNumber();
        this.winStatus = false;
    }

    private int generateRandomNumber() {
        Random rn = new Random();
        return rn.nextInt(100);
    }

    public String compareGuess(int guess) {
        this.numberOfGuesses++;
        if (guess < secretNumber) {
            return "Nope, guess higher. You have made " + this.numberOfGuesses + " guess(es)";
        }
        if (guess > secretNumber) {
            return "Nope, guess lower. You have made " + this.numberOfGuesses + " guess(es)";
        }
        this.winStatus = true;
        return "You made it!!!";

    }

    public String getSessionId() {
        return sessionId;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public boolean getWinStatus() {
        return this.winStatus;
    }
}