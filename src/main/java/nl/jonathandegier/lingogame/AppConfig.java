package nl.jonathandegier.lingogame;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.infrastructure.database.game.postgres.PostgresScoreRepository;
import nl.jonathandegier.lingogame.infrastructure.database.memory.InMemoryGameRepository;
import nl.jonathandegier.lingogame.infrastructure.database.words.postgres.PostgresWordRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {

    @Bean
    public GameRepository getGameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public WordRepository getWordRepository(@Qualifier("wordsEntityManager") EntityManager entityManager) {
        return new PostgresWordRepository(entityManager);
    }

    @Bean
    public ScoreRepository getScoreRepository(@Qualifier("gameEntityManager") EntityManager entityManager) {
        return new PostgresScoreRepository(entityManager);
    }

    @Bean
    public GameService getGameService(GameRepository gameRepository, ScoreRepository scoreRepository, WordRepository wordRepository) {
        return new GameService(wordRepository, scoreRepository, gameRepository);
    }
}
