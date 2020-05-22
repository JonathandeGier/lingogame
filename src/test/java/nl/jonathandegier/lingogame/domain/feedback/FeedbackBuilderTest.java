package nl.jonathandegier.lingogame.domain.feedback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FeedbackBuilder Test")
public class FeedbackBuilderTest {

    @Test
    @DisplayName("Test normal feedback")
    void test_build_normal_feedback() {
        String word = "woord";
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .word(word)
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.GOOD_GUESS);

        Feedback feedback = builder.build();

        assertEquals(guess, feedback.getGuess());
        assertEquals(FeedbackExplaination.GOOD_GUESS, feedback.getExplaination());
        assertEquals(5, feedback.getTotalGuesses());
        assertEquals(5, feedback.getGuessesLeft());
        assertEquals(5, feedback.getWordLength());

        assertEquals(FeedbackType.CORRECT, feedback.getFeedback().get(0).getFeedbackType());
        assertEquals(FeedbackType.CORRECT, feedback.getFeedback().get(1).getFeedbackType());
        assertEquals(FeedbackType.PRESENT, feedback.getFeedback().get(2).getFeedbackType());
        assertEquals(FeedbackType.PRESENT, feedback.getFeedback().get(3).getFeedbackType());
        assertEquals(FeedbackType.ABSENT, feedback.getFeedback().get(4).getFeedbackType());

        char[] guessChars = guess.toCharArray();
        for (int i = 0; i < guessChars.length; i++) {
            assertEquals(guessChars[i], feedback.getFeedback().get(i).getLetter());
        }
    }

    @Test
    @DisplayName("Test invalid feedback")
    void test_build_invalid_feedback() {
        String word = "woord";
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .word(word)
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.WORD_DOES_NOT_EXIST);

        Feedback feedback = builder.build();

        assertEquals(guess, feedback.getGuess());
        assertEquals(FeedbackExplaination.WORD_DOES_NOT_EXIST, feedback.getExplaination());
        assertEquals(5, feedback.getTotalGuesses());
        assertEquals(5, feedback.getGuessesLeft());
        assertEquals(5, feedback.getWordLength());

        char[] guessChars = guess.toCharArray();
        for (int i = 0; i < guessChars.length; i++) {
            assertEquals(guessChars[i], feedback.getFeedback().get(i).getLetter());
            assertEquals(FeedbackType.INVALID, feedback.getFeedback().get(i).getFeedbackType());
        }
    }

    @Test
    @DisplayName("Test get explaination")
    void test_get_explaination() {
        String word = "woord";
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .word(word)
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.OUT_OF_TIME);

        assertEquals(FeedbackExplaination.OUT_OF_TIME, builder.getExplaination());
    }

    @Test
    @DisplayName("Test missing parameters")
    void test_build_missing_parameters() {
        String word = "woord";
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.WORD_DOES_NOT_EXIST);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }
}
