package com.idunno;

import java.util.Random;

public class Model {
    private int numberOfGuesses;
    private int secretNumber;
    private String message;

    public Model() {
        this.numberOfGuesses = 0;
        this.secretNumber = generateRandomNumber();
        this.message = "";
    }

    private int generateRandomNumber() {
        Random rn = new Random();
        return rn.nextInt(100) + 1;
    }

    public void compareGuess(String guessed) {
        int guess = -1;
        try {
            guess = Integer.parseInt(guessed);
        } catch (NumberFormatException ignored) {

        }
        if (guess < 1 || guess > 100) {
            this.message = "Bad input guess is not an integer between 1 - 100";
            return;
        }
        this.numberOfGuesses++;
        if (guess == secretNumber) {
            this.message = "You made it!!!";
            this.secretNumber = generateRandomNumber();
            this.numberOfGuesses = 0;
        } else if (guess < secretNumber) {
            this.message = "Nope, guess higher. You have made " + this.numberOfGuesses + " guess(es)";
        } else {
            this.message = "Nope, guess lower. You have made " + this.numberOfGuesses + " guess(es)";
        }
    }

    public String getMessage() {
        return message;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public int getSecretNumber() {
        return secretNumber;
    }
}