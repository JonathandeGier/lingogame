package nl.jonathandegier.lingogame.domain.score;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Score Test")
public class ScoreTest {

    @Test
    @DisplayName("Test get player")
    void test_get_player() {
        Score score = new Score("player", 100);
        assertEquals("player", score.getPlayer());
    }

    @Test
    @DisplayName("Test get score")
    void test_get_score() {
        Score score = new Score("player", 100);
        assertEquals(100, score.getScore());
    }
}
