package integration;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.application.HighScoreService;
import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.database.game.postgres.PostgresScoreRepository;
import nl.jonathandegier.lingogame.infrastructure.database.memory.InMemoryGameRepository;
import nl.jonathandegier.lingogame.infrastructure.database.words.dto.WordDTO;
import nl.jonathandegier.lingogame.infrastructure.database.words.postgres.PostgresWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ServiceToRepo {

    private Query countQueryMock;
    private Query selectQueryMock;
    private Query checkQueryMock;

    private EntityManager scoreEntityManagerMock;
    private EntityManager wordsEntityManagerMock;

    private HighScoreService highscoreService;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        this.scoreEntityManagerMock = mock(EntityManager.class);

        this.countQueryMock = mock(Query.class);
        when(this.countQueryMock.getSingleResult()).thenReturn(new BigInteger("1000"));

        this.selectQueryMock = mock(Query.class);
        when(this.selectQueryMock.getSingleResult()).thenReturn(new WordDTO("woord", 5));

        this.checkQueryMock = mock(Query.class);
        when(this.checkQueryMock.getSingleResult()).thenReturn(new BigInteger("1"));

        this.wordsEntityManagerMock = mock(EntityManager.class);
        when(this.wordsEntityManagerMock.createNativeQuery("select count(*) from words where length = ?")).thenReturn(this.countQueryMock);
        when(this.wordsEntityManagerMock.createNativeQuery("select * from words where length = ?", WordDTO.class)).thenReturn(this.selectQueryMock);
        when(this.wordsEntityManagerMock.createNativeQuery("select count(*) from words where word = ?")).thenReturn(this.checkQueryMock);

        ScoreRepository scoreRepository = new PostgresScoreRepository(this.scoreEntityManagerMock);
        GameRepository gameRepository = new InMemoryGameRepository();
        WordRepository wordRepository = new PostgresWordRepository(this.wordsEntityManagerMock);

        this.highscoreService = new HighScoreService(scoreRepository);
        this.gameService = new GameService(wordRepository, this.highscoreService, gameRepository);
    }

    @Test
    void test_start_game() {
        int gameId = this.gameService.startGame();
        assertEquals(1, gameId);
    }

    @Test
    void test_start_round() {
        int gameId = this.gameService.startGame();
        Feedback feedback = this.gameService.startRound(gameId);

        assertEquals("w", feedback.getGuess());
        assertEquals(5, feedback.getWordLength());

        verify(this.countQueryMock, times(1)).setParameter(1, 5);
        verify(this.countQueryMock, times(1)).getSingleResult();

        verify(this.selectQueryMock, times(1)).setParameter(1, 5);
        verify(this.selectQueryMock, times(1)).setFirstResult(anyInt());
        verify(this.selectQueryMock, times(1)).setMaxResults(1);
        verify(this.selectQueryMock, times(1)).getSingleResult();
    }

    @Test
    void test_guess() {
        int gameId = this.gameService.startGame();
        this.gameService.startRound(gameId);
        Feedback feedback = this.gameService.guess(gameId, "wordt");

        assertEquals("wordt", feedback.getGuess());
        assertEquals(5, feedback.getWordLength());
        assertEquals(4, feedback.getGuessesLeft());

        verify(this.checkQueryMock, times(1)).setParameter(1, "wordt");
    }

    @Test
    void test_get_score() {
        int gameId = this.gameService.startGame();
        this.gameService.startRound(gameId);
        this.gameService.guess(gameId, "woord");

        assertEquals(50, this.gameService.getScore(gameId));
    }

    @Test
    void test_save_score() {
        int gameId = this.gameService.startGame();
        this.gameService.startRound(gameId);
        this.gameService.guess(gameId, "woord");

        Score score = this.gameService.saveScore(gameId, "player");

        assertEquals(50, score.getScore());
        assertEquals("player", score.getPlayer());

        verify(this.scoreEntityManagerMock, times(1)).persist(any());
    }

    @Test
    void test_end_game() {
        int gameId = this.gameService.startGame();

        this.gameService.endGame(gameId);

        assertThrows(GameNotFoundException.class, () -> {
            this.gameService.startRound(gameId);
        });
    }
}
