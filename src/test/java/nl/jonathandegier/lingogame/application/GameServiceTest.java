package nl.jonathandegier.lingogame.application;

import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("GameService Test")
class GameServiceTest {

    private GameService gameService;

    private WordRepository wordRepositoryMock;
    private GameRepository gameRepositoryMock;
    private HighScoreService highScoreServiceMock;

    private int gameId = 1;

    // setup all the mocks
    @BeforeEach
    void setUp() {
        var wordRepositoryMock = mock(WordRepository.class);
        var gameRepositoryMock = mock(GameRepository.class);
        var highScoreServiceMock = mock(HighScoreService.class);

        when(wordRepositoryMock.randomWord(5)).thenReturn("woord");
        when(wordRepositoryMock.randomWord(6)).thenReturn("woords");
        when(wordRepositoryMock.randomWord(7)).thenReturn("woorden");
        when(wordRepositoryMock.validWord(any())).thenReturn(true);

        when(gameRepositoryMock.getNextGameId()).thenReturn(this.gameId);
        when(gameRepositoryMock.findGame(this.gameId)).thenReturn(new Game(this.gameId, wordRepositoryMock));

        this.wordRepositoryMock = wordRepositoryMock;
        this.gameRepositoryMock = gameRepositoryMock;
        this.highScoreServiceMock = highScoreServiceMock;

        this.gameService = new GameService(this.wordRepositoryMock, this.highScoreServiceMock, this.gameRepositoryMock);
    }

    @Test
    @DisplayName("Test start game")
    void test_start_game() {
        int id =  this.gameService.startGame();

        assertEquals(this.gameId, id);
        verify(this.gameRepositoryMock, times(1)).getNextGameId();
        verify(this.gameRepositoryMock, times(1)).store(any());
    }

    @Test
    @DisplayName("Test start round")
    void test_start_round() {
        Feedback feedback = this.gameService.startRound(this.gameId);

        assertEquals("w", feedback.getGuess());
        verify(this.gameRepositoryMock, times(1)).findGame(this.gameId);
        verify(this.wordRepositoryMock, times(1)).randomWord(anyInt());
    }

    @Test
    @DisplayName("Test guess")
    void test_guess() {
        String guess = "wordt";
        this.gameService.startRound(gameId);
        Feedback feedback = this.gameService.guess(this.gameId, guess);

        assertEquals("wordt", feedback.getGuess());
        verify(this.gameRepositoryMock, times(2)).findGame(this.gameId);
        verify(this.wordRepositoryMock, times(1)).validWord(guess);
    }

    @Test
    @DisplayName("Test get score")
    void test_get_score() {
        int score = this.gameService.getScore(this.gameId);

        assertEquals(0, score);
        verify(this.gameRepositoryMock, times(1)).findGame(this.gameId);
    }

    @Test
    @DisplayName("Test save score")
    void test_save_score() {
        String playerName = "player";
        Score score = this.gameService.saveScore(this.gameId, playerName);

        assertEquals(0, score.getPoints());
        assertEquals(playerName, score.getPlayer());

        verify(this.gameRepositoryMock, times(1)).findGame(this.gameId);
        verify(this.highScoreServiceMock, times(1)).saveScore(score);
        verify(this.gameRepositoryMock, times(1)).deleteGame(this.gameId);
    }

    @Test
    @DisplayName("Test end game")
    void test_end_game() {
        this.gameService.endGame(this.gameId);

        verify(this.gameRepositoryMock, times(1)).deleteGame(this.gameId);
    }
}
