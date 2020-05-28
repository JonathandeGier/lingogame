package nl.jonathandegier.lingogame.infrastructure.database.words.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("WordDTO Test")
public class WordDTOTest {

    @DisplayName("Test empty constructor")
    @Test
    void test_empty_constructor() {
        WordDTO word = new WordDTO();
        assertNull(word.getWord());
    }

    @Test
    @DisplayName("Test get word")
    void test_get_word() {
        WordDTO word = new WordDTO("word", 4);

        assertEquals("word", word.getWord());
    }

    @Test
    @DisplayName("Test get length")
    void test_get_length() {
        WordDTO word = new WordDTO("word", 4);

        assertEquals(4, word.getLength());
    }
}
