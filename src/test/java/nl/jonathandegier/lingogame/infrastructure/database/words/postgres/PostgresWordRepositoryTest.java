package nl.jonathandegier.lingogame.infrastructure.database.words.postgres;

import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.infrastructure.database.words.dto.WordDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("PostgresWordRepository Test")
public class PostgresWordRepositoryTest {

    private static Stream<Arguments> validWords() {
        return Stream.of(
                Arguments.of("woord", "1", true),
                Arguments.of("abcde", "0", false)
        );
    }

    @Test
    @DisplayName("Test get random word")
    void test_random_word() {
        var countQueryMock = mock(Query.class);
        when(countQueryMock.getSingleResult()).thenReturn(new BigInteger("1000"));

        var selectQueryMock = mock(Query.class);
        when(selectQueryMock.getSingleResult()).thenReturn(new WordDTO("woord", 5));

        var entityManagerMock = mock(EntityManager.class);
        when(entityManagerMock.createNativeQuery("select count(*) from words where length = ?")).thenReturn(countQueryMock);
        when(entityManagerMock.createNativeQuery("select * from words where length = ?", WordDTO.class)).thenReturn(selectQueryMock);

        WordRepository repository = new PostgresWordRepository(entityManagerMock);

        String word = repository.randomWord(5);

        assertEquals("woord", word);

        verify(countQueryMock, times(1)).setParameter(1, 5);
        verify(countQueryMock, times(1)).getSingleResult();

        verify(selectQueryMock, times(1)).setParameter(1, 5);
        verify(selectQueryMock, times(1)).setFirstResult(anyInt());
        verify(selectQueryMock, times(1)).setMaxResults(1);
        verify(selectQueryMock, times(1)).getSingleResult();
    }

    @ParameterizedTest
    @MethodSource("validWords")
    @DisplayName("Test valid word (true and false case)")
    void test_valid_word_true(String word, String resultCount, boolean expected) {
        var queryMock = mock(Query.class);
        when(queryMock.getSingleResult()).thenReturn(new BigInteger(resultCount));

        var entityManagerMock = mock(EntityManager.class);
        when(entityManagerMock.createNativeQuery(any())).thenReturn(queryMock);

        WordRepository repository = new PostgresWordRepository(entityManagerMock);

        boolean result = repository.validWord(word);

        assertEquals(expected, result);
        verify(queryMock, times(1)).setParameter(1, word);
    }
}
