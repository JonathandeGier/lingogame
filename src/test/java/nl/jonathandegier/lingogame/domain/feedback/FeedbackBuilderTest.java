package nl.jonathandegier.lingogame.domain.feedback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FeedbackBuilder Test")
public class FeedbackBuilderTest {

    private static final FeedbackType CORRECT = FeedbackType.CORRECT;
    private static final FeedbackType PRESENT = FeedbackType.PRESENT;
    private static final FeedbackType ABSENT = FeedbackType.ABSENT;
    private static final FeedbackType INVALID = FeedbackType.INVALID;

    private static Stream<Arguments> feedbackWords() {
        // correct word, guess, expected feedback
        return Stream.of(
                Arguments.of("woord", "wordt", List.of(CORRECT, CORRECT, PRESENT, PRESENT, ABSENT)),
                Arguments.of("appel", "panda", List.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("baard", "barst", List.of(CORRECT, CORRECT, PRESENT, ABSENT, ABSENT)),
                Arguments.of("baard", "barak", List.of(CORRECT, CORRECT, PRESENT, PRESENT, ABSENT)),
                Arguments.of("barst", "barak", List.of(CORRECT, CORRECT, CORRECT, ABSENT, ABSENT)),
                Arguments.of("abcde", "deade", List.of(ABSENT, ABSENT, PRESENT, CORRECT, CORRECT))
        );
    }

    @ParameterizedTest
    @MethodSource("feedbackWords")
    @DisplayName("Test normal feedback")
    void test_build_feedback(String word, String guess, List<FeedbackType> expectedFeedback) {
        var builder = new FeedbackBuilder()
                .word(word)
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.GOOD_GUESS);

        Feedback feedback = builder.build();

        assertBasicFeedback(feedback, guess, FeedbackExplaination.GOOD_GUESS, 5, 5, 5);
        assertFeedbackArray(expectedFeedback, feedback);
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

        assertBasicFeedback(feedback, guess, FeedbackExplaination.WORD_DOES_NOT_EXIST, 5, 5, 5);
        assertFeedbackArray(List.of(INVALID, INVALID, INVALID, INVALID, INVALID), feedback);
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
    @DisplayName("Test missing word")
    void test_build_missing_word() {
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .guess(guess, 5, 5)
                .explaination(FeedbackExplaination.WORD_DOES_NOT_EXIST);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    @Test
    @DisplayName("Test missing guess")
    void test_build_missing_guess() {
        String word = "woord";
        var builder = new FeedbackBuilder()
                .word(word)
                .explaination(FeedbackExplaination.WORD_DOES_NOT_EXIST);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    @Test
    @DisplayName("Test missing explaination")
    void test_build_missing_explaination() {
        String word = "woord";
        String guess = "wordt";
        var builder = new FeedbackBuilder()
                .word(word)
                .guess(guess, 5, 5);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    private void assertBasicFeedback(
            Feedback feedback,
            String expectedGuess,
            FeedbackExplaination expectedExplaination,
            int expectedGuesses,
            int expectedGuessesLeft,
            int expectedWordLength) {
        assertEquals(expectedGuess, feedback.getGuess());
        assertEquals(expectedExplaination, feedback.getExplaination());
        assertEquals(expectedGuesses, feedback.getTotalGuesses());
        assertEquals(expectedGuessesLeft, feedback.getGuessesLeft());
        assertEquals(expectedWordLength, feedback.getWordLength());
    }

    private void assertFeedbackArray(List<FeedbackType> expectedTypes, Feedback feedback) {
        for (int i = 0; i < expectedTypes.size(); i++) {
            assertEquals(feedback.getGuess().charAt(i), feedback.getFeedback().get(i).getLetter());
            assertEquals(expectedTypes.get(i), feedback.getFeedback().get(i).getFeedbackType());
        }
    }
}
