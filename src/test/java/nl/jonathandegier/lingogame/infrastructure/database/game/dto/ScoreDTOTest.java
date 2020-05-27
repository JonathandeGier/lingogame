package nl.jonathandegier.lingogame.infrastructure.database.game.dto;

import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreDTOTest {

    private static Stream<Arguments> equalObjects() {
        return Stream.of(
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(1L, "player", 100), true),   // equal
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(1L, "player", 200), false),  // score different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(1L, "player2", 100), false), // player different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(2L, "player", 100), false),  // id different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(1L, "player2", 200), false), // player and score different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(2L, "player2", 100), false), // player and id different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(2L, "player", 200), false),  // id and score different
                Arguments.of(new ScoreDTO(1L, "player", 100), new ScoreDTO(2L, "player2", 200), false), // everything different
                Arguments.of(new ScoreDTO(1L, "player", 100), new Score("player", 100), false)              // different object
        );
    }

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

    @ParameterizedTest
    @MethodSource("equalObjects")
    void test_equals(ScoreDTO score, Object other, boolean expected) {
        assertEquals(expected, score.equals(other));
    }

    @ParameterizedTest
    @MethodSource("equalObjects")
    void test_hashCode(ScoreDTO score, Object other, boolean expected) {
        assertEquals(expected, score.hashCode() == other.hashCode());
    }
}
