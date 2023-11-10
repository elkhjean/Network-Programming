package com.guessinggame.model;

import java.util.Random;

public class GuessingGame {
    private String sessionId;
    private int numberOfGuesses;
    private int secretNumber;

    public GuessingGame(String sessionId) {
        this.sessionId = sessionId;
        this.numberOfGuesses = 0;
        this.secretNumber = generateRandomNumber();
    }

    private int generateRandomNumber() {
        Random rn = new Random();
        return rn.nextInt(100);
    }

    public boolean checkNumber(int guess) {
        if (guess == secretNumber)
            return true;
        this.numberOfGuesses++;
        return false;

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

}
