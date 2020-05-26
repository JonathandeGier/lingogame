package nl.jonathandegier.lingogame.infrastructure.database.memory;

import nl.jonathandegier.lingogame.application.exceptions.GameIdAlreadyExistsException;
import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryGameRepository Test")
public class InMemoryGameRepositoryTest {

    @Test
    @DisplayName("Test store and get game")
    void test_store_and_get_game() {
        GameRepository repository = new InMemoryGameRepository();

        Game game1 = new Game(1, null);
        Game game2 = new Game(2, null);

        repository.store(game1);
        repository.store(game2);

        Game foundGame1 = repository.findGame(1);
        Game foundGame2 = repository.findGame(2);

        assertEquals(game1, foundGame1);
        assertEquals(game2, foundGame2);
    }

    @Test
    @DisplayName("Test game not found")
    void test_game_not_found() {
        GameRepository repository = new InMemoryGameRepository();

        assertThrows(GameNotFoundException.class, () -> {
            repository.findGame(1);
        });
    }

    @Test
    @DisplayName("test store a duplicate id")
    void test_store_duplicate_ids() {
        GameRepository repository = new InMemoryGameRepository();

        Game game1 = new Game(1, null);
        Game game2 = new Game(1, null);

        repository.store(game1);

        assertThrows(GameIdAlreadyExistsException.class, () -> {
            repository.store(game2);
        });
    }

    // property based test
    @Test
    @DisplayName("Test netId will give a unique id each time")
    void test_nextId_gives_unique_ids() {
        GameRepository repository = new InMemoryGameRepository();
        Set<Integer> idSet = new HashSet<>();
        int generatedIdCount = 100;

        for (int i = 0; i < generatedIdCount; i++) {
            // values in a Set will always be unique
            idSet.add(repository.getNextGameId());
        }

        assertEquals(generatedIdCount, idSet.size());
    }

    @Test
    @DisplayName("Test delete game")
    void test_delete_game() {
        GameRepository repository = new InMemoryGameRepository();
        Game game = new Game(1, null);
        repository.store(game);

        repository.deleteGame(1);

        assertThrows(GameNotFoundException.class, () -> {
            repository.findGame(1);
        });
    }

    @Test
    @DisplayName("Test delete non-existing game")
    void test_delete_non_existing_game() {
        GameRepository repository = new InMemoryGameRepository();

        repository.deleteGame(1);

        assertThrows(GameNotFoundException.class, () -> {
            repository.findGame(1);
        });
    }
}
