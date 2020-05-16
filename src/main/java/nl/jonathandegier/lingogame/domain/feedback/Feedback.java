package nl.jonathandegier.lingogame.domain.feedback;

import java.util.List;

public class Feedback {

    private String guess;
    private List<FeedbackElement> feedback;
    private FeedbackExplaination explaination;

    public Feedback(String guess, List<FeedbackElement> feedback, FeedbackExplaination explaination) {
        this.guess = guess;
        this.feedback = feedback;
        this.explaination = explaination;
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
}
