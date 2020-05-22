package nl.jonathandegier.lingogame.domain.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {

    @Test
    void test_get_player() {
        Score score = new Score("player", 100);
        assertEquals("player", score.getPlayer());
    }

    @Test
    void test_get_score() {
        Score score = new Score("player", 100);
        assertEquals(100, score.getScore());
    }
}
