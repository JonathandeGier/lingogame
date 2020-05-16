package nl.jonathandegier.lingogame.domain.feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackBuilder {

    private String word;
    private String guess;
    private FeedbackExplaination explaination;

    public FeedbackBuilder word(String word) {
        this.word = word;
        return this;
    }

    public FeedbackBuilder guess(String guess) {
        this.guess = guess;
        return this;
    }

    public FeedbackBuilder explaination(FeedbackExplaination explaination) {
        this.explaination = explaination;
        return this;
    }

    public FeedbackExplaination getExplaination() {
        return this.explaination;
    }

    public Feedback build() {
        if (this.word == null || this.guess == null || this.explaination == null) {
            throw new IllegalArgumentException("Missing required parameters");
        }

        List<FeedbackElement> feedbackElements;
        if (explaination == FeedbackExplaination.GOOD_GUESS || explaination == FeedbackExplaination.CORRECT) {
            feedbackElements = normalFeedback();
        } else {
            feedbackElements = invalidFeedback();
        }

        return new Feedback(this.guess, feedbackElements, this.explaination);
    }

    // TODO: Strategy pattern?
    private List<FeedbackElement> normalFeedback() {
        char[] wordChars = this.word.toCharArray();
        char[] guessChars = this.guess.toCharArray();

        List<FeedbackElement> feedback = new ArrayList<>();

        for (int i = 0; i < wordChars.length; i++) {
            FeedbackType type;
            if (wordChars[i] == guessChars[i]) {
                type = FeedbackType.CORRECT;
            } else if (this.word.contains(Character.toString(guessChars[i]))) {
                type = FeedbackType.PRESENT;
            } else {
                type = FeedbackType.ABSENT;
            }

            feedback.add(new FeedbackElement(guessChars[i], type));
        }

        return feedback;
    }

    private List<FeedbackElement> invalidFeedback() {
        char[] guessChars = this.guess.toCharArray();

        List<FeedbackElement> feedback = new ArrayList<>();

        for (int i = 0; i < this.word.length(); i++) {

            if (i < guessChars.length) {
                feedback.add(new FeedbackElement(guessChars[i], FeedbackType.INVALID));
            } else {
                feedback.add(new FeedbackElement('0', FeedbackType.INVALID));
            }
        }

        return feedback;
    }
}