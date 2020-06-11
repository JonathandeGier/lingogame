package nl.jonathandegier.lingogame.domain.feedback;

import java.util.List;

public class Feedback {

    private String guess;
    private List<FeedbackElement> feedbackElements;
    private FeedbackExplaination explaination;
    private int totalGuesses;
    private int guessesLeft;
    private int wordLength;

    public Feedback(String guess, List<FeedbackElement> feedback, FeedbackExplaination explaination, int totalGuesses, int guessesLeft, int wordLength) {
        this.guess = guess;
        this.feedbackElements = feedback;
        this.explaination = explaination;
        this.totalGuesses = totalGuesses;
        this.guessesLeft = guessesLeft;
        this.wordLength = wordLength;
    }

    public String getGuess() {
        return this.guess;
    }

    public List<FeedbackElement> getFeedback() {
        return this.feedbackElements;
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

    public int getWordLength() {
        return wordLength;
    }
}
