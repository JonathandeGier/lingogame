package nl.jonathandegier.lingogame;

import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.infrastructure.database.memory.InMemoryGameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GameRepository getGameRepository() {
        return new InMemoryGameRepository();
    }
}
