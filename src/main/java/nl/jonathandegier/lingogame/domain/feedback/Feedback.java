package nl.jonathandegier.lingogame.domain.feedback;

import java.util.List;

public class Feedback {

    private String guess;
    private List<FeedbackElement> feedback;
    private FeedbackExplaination explaination;
    private int totalGuesses;
    private int guessesLeft;

    public Feedback(String guess, List<FeedbackElement> feedback, FeedbackExplaination explaination, int totalGuesses, int guessesLeft) {
        this.guess = guess;
        this.feedback = feedback;
        this.explaination = explaination;
        this.totalGuesses = totalGuesses;
        this.guessesLeft = guessesLeft;
    }

    public String getGuess() {
        return this.guess;
    }

    public List<FeedbackElement> getFeedback() {
        return this.feedback;
    }

    public FeedbackExplaination getExplaination() {
        return this.explaination;
    }

    public int getTotalGuesses() {
        return totalGuesses;
    }

    public int getGuessesLeft() {
        return guessesLeft;
    }
}
