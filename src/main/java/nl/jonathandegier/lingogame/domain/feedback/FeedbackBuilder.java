package nl.jonathandegier.lingogame.domain.feedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackBuilder {

    private String word;
    private String guess;
    private FeedbackExplaination explaination;
    private int totalGuesses;
    private int guessesLeft;

    public FeedbackBuilder word(String word) {
        this.word = word;
        return this;
    }

    public FeedbackBuilder guess(String guess, int totalGuesses, int guessesLeft) {
        this.guess = guess;
        this.totalGuesses = totalGuesses;
        this.guessesLeft = guessesLeft;
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
            throw new IllegalArgumentException("Missing required parameters to make feedback");
        }

        List<FeedbackElement> feedbackElements;
        if (explaination == FeedbackExplaination.GOOD_GUESS
                || explaination == FeedbackExplaination.CORRECT
                || explaination == FeedbackExplaination.GAME_OVER) {
            feedbackElements = normalFeedback();
        } else {
            feedbackElements = invalidFeedback();
        }

        return new Feedback(this.guess, feedbackElements, this.explaination, this.totalGuesses, this.guessesLeft, this.word.length());
    }

    private List<FeedbackElement> normalFeedback() {
        // make a list out of the characters of the word
        List<Character> correctAndPresendCharacters = new ArrayList<>();
        for (char c : this.word.toCharArray()) {
            correctAndPresendCharacters.add(c);
        }

        // temporary store the elements in a map
        // since the correct characters will be added first, we will need to order them later, so we set the index as key
        Map<Integer, FeedbackElement> unOrderedElements = new HashMap<>();

        // get all the correct characters out or the list first
        for (int i = 0; i < this.guess.length(); i++) {
            if (this.word.charAt(i) == this.guess.charAt(i)) {
                correctAndPresendCharacters.remove(Character.valueOf(this.guess.charAt(i)));
                unOrderedElements.put(i, new FeedbackElement(this.guess.charAt(i), FeedbackType.CORRECT));
            }
        }

        // go over the guessed characters again,
        // if the character is still in the correctAndPresentCharacters list, then it is present, else absent
        for (int i = 0; i < this.guess.length(); i++) {
            if (this.word.charAt(i) != this.guess.charAt(i)) {
                if (correctAndPresendCharacters.contains(this.guess.charAt(i))) {
                    correctAndPresendCharacters.remove(Character.valueOf(this.guess.charAt(i)));
                    unOrderedElements.put(i, new FeedbackElement(this.guess.charAt(i), FeedbackType.PRESENT));
                } else {
                    unOrderedElements.put(i, new FeedbackElement(this.guess.charAt(i), FeedbackType.ABSENT));
                }
            }
        }

        // order the elements
        List<FeedbackElement> feedback = new ArrayList<>();
        for (int i = 0; i < this.guess.length(); i++) {
            feedback.add(unOrderedElements.get(i));
        }

        return feedback;
    }

    private List<FeedbackElement> invalidFeedback() {
        char[] guessChars = this.guess.toCharArray();

        List<FeedbackElement> feedback = new ArrayList<>();

        for (char letter : guessChars) {
            feedback.add(new FeedbackElement(letter, FeedbackType.INVALID));
        }

        return feedback;
    }
}
