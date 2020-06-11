package nl.jonathandegier.lingogame.infrastructure.api.v1;

import nl.jonathandegier.lingogame.application.HighScoreService;
import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HighscoreControllerTest {

    @Test
    void test_get_scores() {
        List<Score> expectedScores = List.of(
                new Score("player1", 200),
                new Score("player2", 100)
        );

        var highscoreServiceMock = mock(HighScoreService.class);
        when(highscoreServiceMock.getHighscores()).thenReturn(expectedScores);

        var controller = new HighscoreController(highscoreServiceMock);

        List<Score> scores = controller.getScores();

        assertEquals(expectedScores, scores);
        verify(highscoreServiceMock, times(1)).getHighscores();
    }
}
