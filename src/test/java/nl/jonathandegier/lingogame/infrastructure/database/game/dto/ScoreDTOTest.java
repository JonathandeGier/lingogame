package nl.jonathandegier.lingogame.infrastructure.database.game.dto;

import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreDTOTest {

    @Test
    void test_empty_constructor() {
        ScoreDTO score = new ScoreDTO();
        assertNull(score.getName());
    }

    @Test
    void test_construct_from_score() {
        Score score = new Score("player", 100);
        ScoreDTO scoreDTO = new ScoreDTO(score);

        assertEquals("player", scoreDTO.getName());
        assertEquals(100, scoreDTO.getScore());
    }

    @Test
    void test_get_id() {
        var score = new ScoreDTO(1L, "player", 100);
        assertEquals(1L, score.getId());
    }

    @Test
    void test_get_name() {
        var score = new ScoreDTO(1L, "player", 100);
        assertEquals("player", score.getName());
    }

    @Test
    void test_get_score() {
        var score = new ScoreDTO(1L, "player", 100);
        assertEquals(100, score.getScore());
    }
}
