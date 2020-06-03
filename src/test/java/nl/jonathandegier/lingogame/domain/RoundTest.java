package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.exceptions.GameIsOverException;
import nl.jonathandegier.lingogame.domain.exceptions.RoundNotStartedException;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackExplaination;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Round Test")
public class RoundTest {

    private static Stream<Arguments> scoresWithAmountOfGuesses() {
        return Stream.of(
                Arguments.of(1, 50),
                Arguments.of(2, 40),
                Arguments.of(3, 30),
                Arguments.of(4, 20),
                Arguments.of(5, 10)
        );
    }

    private static Stream<Arguments> simpleGuesses() {
        return Stream.of(
                Arguments.of("wordt", FeedbackExplaination.GOOD_GUESS),
                Arguments.of("abcde", FeedbackExplaination.WORD_DOES_NOT_EXIST),
                Arguments.of("worden", FeedbackExplaination.WORD_TOO_LONG),
                Arguments.of("word", FeedbackExplaination.WORD_TOO_SHORT),
                Arguments.of("woord", FeedbackExplaination.CORRECT)
        );
    }

    @Test
    @DisplayName("Test start round")
    void test_start_round() {
        Round round = new Round(wordRepositoryMock(), 5);

        Feedback feedback = round.startRound();

        assertEquals("w", feedback.getGuess());
        assertEquals(FeedbackExplaination.GOOD_GUESS, feedback.getExplaination());
        assertEquals(5, feedback.getWordLength());
        assertEquals(5, feedback.getGuessesLeft());
        assertEquals(5, feedback.getTotalGuesses());

        assertEquals(1, feedback.getFeedback().size());
        assertEquals('w', feedback.getFeedback().get(0).getLetter());
        assertEquals(FeedbackType.CORRECT, feedback.getFeedback().get(0).getFeedbackType());
    }

    @ParameterizedTest
    @MethodSource("scoresWithAmountOfGuesses")
    @DisplayName("Test calculate score")
    void test_calculate_score(int guesses, int expectedScore) {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();

        for (int i = 0; i < guesses; i++) {
            round.guess("short");
        }

        assertEquals(expectedScore, round.calculateScore());
    }

    @Test
    @DisplayName("Test get word length")
    void test_get_word_length() {
        Round round = new Round(wordRepositoryMock(), 5);
        assertEquals(5, round.getWordLength());
    }

    @Test
    @DisplayName("Test round not completed")
    void test_round_not_completed() {
        Round round = new Round(wordRepositoryMock(), 5);
        assertFalse(round.roundCompleted());
    }

    @Test
    @DisplayName("Test round completed")
    void test_round_completed() {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();
        round.guess("woord");
        assertTrue(round.roundCompleted());
    }


    @ParameterizedTest
    @MethodSource("simpleGuesses")
    @DisplayName("Test simple guesses")
    void test_simple_guesses(String guess, FeedbackExplaination expectedExplaination) {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();
        Feedback feedback = round.guess(guess);
        assertEquals(expectedExplaination, feedback.getExplaination());
    }

    @Test
    @DisplayName("Test guess out of time")
    void test_guess_OUT_OF_TIME() {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();

        Instant start = Instant.now();
        await().atMost(11, TimeUnit.SECONDS).until(() -> Instant.now().minusSeconds(10).isAfter(start));

        Feedback feedback = round.guess("wordt");
        assertEquals(FeedbackExplaination.OUT_OF_TIME, feedback.getExplaination());
    }

    @Test
    @DisplayName("Test last guess game over")
    void test_guess_GAME_OVER() {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();

        // 5 incorrect guesses
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        Feedback feedback = round.guess("wordt");

        assertEquals(FeedbackExplaination.GAME_OVER, feedback.getExplaination());
    }

    @Test
    @DisplayName("Test guess correct on last try")
    void test_guess_CORRECT_last_try() {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();

        // 5 incorrect guesses
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        Feedback feedback = round.guess("woord");

        assertEquals(FeedbackExplaination.CORRECT, feedback.getExplaination());
    }

    @Test
    @DisplayName("Test guess while round not started")
    void test_not_started_throws_exception() {
        Round round = new Round(wordRepositoryMock(), 5);

        assertThrows(RoundNotStartedException.class, () -> {
            round.guess("woord");
        });
    }

    @Test
    @DisplayName("Test out of guesses")
    void test_out_of_guesses_throws_exception() {
        Round round = new Round(wordRepositoryMock(), 5);
        round.startRound();

        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");
        round.guess("wordt");

        assertThrows(GameIsOverException.class, () -> {
            round.guess("woord");
        });
    }

    private WordRepository wordRepositoryMock() {
        var wordRepositoryMock = mock(WordRepository.class);
        when(wordRepositoryMock.randomWord(5)).thenReturn("woord");
        when(wordRepositoryMock.randomWord(6)).thenReturn("woords");
        when(wordRepositoryMock.randomWord(7)).thenReturn("woorden");

        when(wordRepositoryMock.validWord(any())).thenReturn(true);
        when(wordRepositoryMock.validWord("abcde")).thenReturn(false);

        return wordRepositoryMock;
    }
}
