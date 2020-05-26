package nl.jonathandegier.lingogame.infrastructure.database.memory;

import nl.jonathandegier.lingogame.application.exceptions.GameIdAlreadyExistsException;
import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.GameRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryGameRepository implements GameRepository {

    private Map<Integer, Game> games;
    private int idCounter;

    public InMemoryGameRepository() {
        this.games = new HashMap<>();
        this.idCounter = 0;
    }

    @Override
    public void store(Game game) {
        if (getGame(game.getId()) != null) {
            throw new GameIdAlreadyExistsException(String.format("Game with id: %s already exists", game.getId()));
        }

        this.games.put(game.getId(), game);
    }

    @Override
    public Game findGame(int gameId) {
        Game game = getGame(gameId);

        if (game == null) {
            throw new GameNotFoundException(String.format("Game with id: %s could not be found", gameId));
        }

        return game;
    }

    @Override
    public int getNextGameId() {
        this.idCounter++;
        return this.idCounter;
    }

    @Override
    public void deleteGame(int gameId) {
        this.games.remove(gameId);
    }

    private Game getGame(int gameId) {
        return this.games.get(gameId);
    }
}
