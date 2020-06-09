package nl.jonathandegier.lingogame;

import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.infrastructure.database.game.postgres.PostgresScoreRepository;
import nl.jonathandegier.lingogame.infrastructure.database.memory.InMemoryGameRepository;
import nl.jonathandegier.lingogame.infrastructure.database.words.dto.WordDTO;
import nl.jonathandegier.lingogame.infrastructure.database.words.postgres.PostgresWordRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WordRepository getTestWordRepository(@Qualifier("wordsEntityManager") EntityManager entityManager) {
        var countQueryMock = mock(Query.class);
        when(countQueryMock.getSingleResult()).thenReturn(new BigInteger("1000"));

        var selectQueryMock = mock(Query.class);
        when(selectQueryMock.getSingleResult()).thenReturn(new WordDTO("woord", 5));

        var checkQueryMock = mock(Query.class);
        when(checkQueryMock.getSingleResult()).thenReturn(new BigInteger("1"));

        var wordsEntityManagerMock = mock(EntityManager.class);
        when(wordsEntityManagerMock.createNativeQuery("select count(*) from words where length = ?")).thenReturn(countQueryMock);
        when(wordsEntityManagerMock.createNativeQuery("select * from words where length = ?", WordDTO.class)).thenReturn(selectQueryMock);
        when(wordsEntityManagerMock.createNativeQuery("select count(*) from words where word = ?")).thenReturn(checkQueryMock);

        return new PostgresWordRepository(wordsEntityManagerMock);
    }
}
