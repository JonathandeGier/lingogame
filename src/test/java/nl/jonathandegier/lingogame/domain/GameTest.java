package nl.jonathandegier.lingogame.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Game Test")
public class GameTest {

    @Test
    @DisplayName("Test get ID")
    void test_get_id() {
        int id = 1;
        Game game = new Game(id, null);
        assertEquals(id, game.getId());
    }

    @Test
    @DisplayName("Test round length sequence")
    void test_round_word_lengths() {
        var wordRepositoryMock = wordRepositoryMock();

        Game game = new Game(1, wordRepositoryMock);

        var feedback1 = game.newRound();
        game.guess("woord");
        var feedback2 = game.newRound();
        game.guess("woords");
        var feedback3 = game.newRound();
        game.guess("woorden");
        var feedback4 = game.newRound();

        assertEquals(5, feedback1.getWordLength());
        assertEquals(6, feedback2.getWordLength());
        assertEquals(7, feedback3.getWordLength());
        assertEquals(5, feedback4.getWordLength());
    }

    @Test
    @DisplayName("Test add round while uncompleted")
    void test_new_round_while_uncompleted() {
        var wordRepositoryMock = wordRepositoryMock();

        Game game = new Game(1, wordRepositoryMock);
        game.newRound();

        assertThrows(IllegalArgumentException.class, () -> {
            game.newRound();
        });
    }

    @Test
    @DisplayName("Test guess")
    void test_guess() {
        var wordRepositoryMock = wordRepositoryMock();

        Game game = new Game(1, wordRepositoryMock);

        game.newRound();
        var feedback = game.guess("woord");

        assertEquals(5, feedback.getWordLength());
        assertEquals("woord", feedback.getGuess());
    }

    @Test
    @DisplayName("Test calculate score")
    void test_calculate_score() {
        var wordRepositoryMock = wordRepositoryMock();

        Game game = new Game(1, wordRepositoryMock);

        game.newRound();
        game.guess("woord");

        assertEquals(50, game.calculateScore());
    }

    private WordRepository wordRepositoryMock() {
        var wordRepositoryMock = mock(WordRepository.class);
        when(wordRepositoryMock.randomWord(5)).thenReturn("woord");
        when(wordRepositoryMock.randomWord(6)).thenReturn("woords");
        when(wordRepositoryMock.randomWord(7)).thenReturn("woorden");

        when(wordRepositoryMock.validWord(any())).thenReturn(true);

        return wordRepositoryMock;
    }
}
