package nl.jonathandegier.lingogame.infrastructure.api.v1;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.exceptions.GameIsOverException;
import nl.jonathandegier.lingogame.domain.exceptions.RoundNotStartedException;
import nl.jonathandegier.lingogame.domain.exceptions.UncompletedRoundException;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.errors.ErrorBody;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.errors.ErrorType;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.GuessRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.SaveScoreRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses.GameCreatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    private GameService gameServiceMock;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        this.gameServiceMock = mock(GameService.class);
        this.gameController = new GameController(this.gameServiceMock);
    }

    @Test
    void test_start_game() {
        when(this.gameServiceMock.startGame()).thenReturn(1);
        GameCreatedResponse response = this.gameController.startGame();

        assertEquals(1, response.gameId);
        verify(this.gameServiceMock, times(1)).startGame();
    }

    @Test
    void test_start_round() {
        var expectedFeedback = new Feedback("w", null, null, 5, 5 ,5);
        when(this.gameServiceMock.startRound(anyInt())).thenReturn(expectedFeedback);
        Feedback response = this.gameController.startRound(1);

        assertEquals(expectedFeedback, response);
        verify(this.gameServiceMock, times(1)).startRound(1);
    }

    @Test
    void test_guess() {
        var expectedFeedback = new Feedback("wordt", null, null, 5, 4 ,5);
        var guessRequest = new GuessRequest();
        guessRequest.guess = "wordt";

        when(this.gameServiceMock.guess(anyInt(), eq("wordt"))).thenReturn(expectedFeedback);
        Feedback response = this.gameController.guess(1, guessRequest);

        assertEquals(expectedFeedback, response);
        verify(this.gameServiceMock, times(1)).guess(1, "wordt");
    }

    @Test
    void test_get_score() {
        when(this.gameServiceMock.getScore(anyInt())).thenReturn(50);
        Score response = this.gameController.getScore(1);

        assertEquals(50, response.getScore());
        verify(this.gameServiceMock, times(1)).getScore(1);
    }

    @Test
    void test_save_score() {
        var expectedScore = new Score("player", 50);
        when(this.gameServiceMock.saveScore(anyInt(), anyString())).thenReturn(expectedScore);

        var saveRequest = new SaveScoreRequest();
        saveRequest.name = "player";

        var response = this.gameController.saveScore(1, saveRequest);

        assertEquals(200, response.status);
        assertEquals(expectedScore, response.finalScore);
        verify(this.gameServiceMock, times(1)).saveScore(1, "player");
    }

    @Test
    void test_end_game() {
        var response = this.gameController.endGame(1);

        assertEquals(200, response.status);
        verify(this.gameServiceMock, times(1)).endGame(1);
    }

    @Test
    void test_handle_game_not_found_exception() {
        String message = "Game not found";
        GameNotFoundException e = new GameNotFoundException(message);

        var response = this.gameController.handleGameNotFoundException(e);
        var body = (ErrorBody) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, body.statusInt);
        assertEquals(message, body.message);
        assertEquals(ErrorType.GAME_NOT_FOUND, body.type);
    }

    @Test
    void test_handle_game_is_over_exception() {
        String message = "Game is over";
        GameIsOverException e = new GameIsOverException(message);

        var response = this.gameController.handleGameIsOverException(e);
        var body = (ErrorBody) response.getBody();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(406, body.statusInt);
        assertEquals(message, body.message);
        assertEquals(ErrorType.GAME_ALREADY_OVER, body.type);
    }

    @Test
    void test_handle_round_not_started_exception() {
        String message = "Round not started";
        RoundNotStartedException e = new RoundNotStartedException(message);

        var response = this.gameController.handleRoundNotStartedException(e);
        var body = (ErrorBody) response.getBody();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(406, body.statusInt);
        assertEquals(message, body.message);
        assertEquals(ErrorType.ROUND_NOT_STARTED, body.type);
    }

    @Test
    void test_handle_uncompleted_round_exception() {
        String message = "Uncompleted round";
        UncompletedRoundException e = new UncompletedRoundException(message);

        var response = this.gameController.handleUncompletedRoundException(e);
        var body = (ErrorBody) response.getBody();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(406, body.statusInt);
        assertEquals(message, body.message);
        assertEquals(ErrorType.UNCOMPLETED_ROUND, body.type);
    }
}
