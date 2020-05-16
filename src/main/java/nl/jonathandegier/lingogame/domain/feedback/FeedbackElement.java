package nl.jonathandegier.lingogame.domain.feedback;

public class FeedbackElement {

    private char letter;
    private FeedbackType feedbackType;

    public FeedbackElement(char letter, FeedbackType feedbackType) {
        this.letter = letter;
        this.feedbackType = feedbackType;
    }

    public char getLetter() {
        return this.letter;
    }

    public FeedbackType getFeedbackType() {
        return this.feedbackType;
    }
}
