package nl.jonathandegier.lingogame.infrastructure.database.game.postgres;

import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.database.game.dto.ScoreDTO;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostgresScoreRepositoryTest {

    @Test
    void test_get_scores() {
        var queryMock = mock(Query.class);
        when(queryMock.getResultList()).thenReturn(List.of(
                new ScoreDTO(1L, "player", 200),
                new ScoreDTO(2L, "player2", 100)
        ));

        var entityManagerMock = mock(EntityManager.class);
        when(entityManagerMock.createNativeQuery("select * from scores order by score desc", ScoreDTO.class)).thenReturn(queryMock);

        var repository = new PostgresScoreRepository(entityManagerMock);

        List<Score> scores = repository.getScores();

        assertEquals("player", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getScore());
        assertEquals("player2", scores.get(1).getPlayer());
        assertEquals(100, scores.get(1).getScore());

        verify(queryMock, times(1)).getResultList();
    }

    @Test
    void test_store_score() {
        var entityManagerMock = mock(EntityManager.class);
        var repository = new PostgresScoreRepository(entityManagerMock);
        Score score = new Score("player", 100);

        repository.storeScore(score);

        verify(entityManagerMock, times(1)).persist(any());
    }
}
