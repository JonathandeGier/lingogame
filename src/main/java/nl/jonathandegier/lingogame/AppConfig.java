package nl.jonathandegier.lingogame;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.infrastructure.database.memory.InMemoryGameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GameRepository getGameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public WordRepository getWordRepository() {
        return null;
    }

    @Bean
    public ScoreRepository getScoreRepository() {
        return null;
    }

    @Bean
    public GameService getGameService(GameRepository gameRepository, ScoreRepository scoreRepository, WordRepository wordRepository) {
        return new GameService(wordRepository, scoreRepository, gameRepository);
    }
}
