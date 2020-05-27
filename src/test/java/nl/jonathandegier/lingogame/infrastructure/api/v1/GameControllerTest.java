package nl.jonathandegier.lingogame.infrastructure.api.v1;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.GuessRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.SaveScoreRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses.GameCreatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
