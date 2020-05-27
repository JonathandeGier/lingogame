package nl.jonathandegier.lingogame.application;

import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HighScoreServiceTest {

    @Test
    void test_get_scores() {
        List<Score> expectedScores = List.of(
                new Score("player1", 200),
                new Score("player2", 100)
        );

        var scoreRepositoryMock = mock(ScoreRepository.class);
        when(scoreRepositoryMock.getScores()).thenReturn(expectedScores);

        var service = new HighScoreService(scoreRepositoryMock);

        List<Score> scores = service.getHighscores();

        assertEquals(expectedScores, scores);
        verify(scoreRepositoryMock, times(1)).getScores();
    }

    @Test
    void test_store_score() {
        var scoreRepositoryMock = mock(ScoreRepository.class);
        var service = new HighScoreService(scoreRepositoryMock);

        Score score = new Score("player", 100);

        service.saveScore(score);

        verify(scoreRepositoryMock, times(1)).storeScore(score);
    }
}
