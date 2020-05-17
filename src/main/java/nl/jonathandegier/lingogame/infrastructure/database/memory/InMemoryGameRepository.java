package nl.jonathandegier.lingogame.infrastructure.database.memory;

import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.GameRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryGameRepository implements GameRepository {

    private List<Game> games;
    private int idCounter;

    public InMemoryGameRepository() {
        this.games = new ArrayList<>();
        this.idCounter = 0;
    }

    @Override
    public void store(Game game) {
        this.games.add(game);
    }

    @Override
    public Game findGame(int gameId) {
        for (Game g : this.games) {
            if (g.getId() == gameId) {
                return g;
            }
        }

        throw new GameNotFoundException(String.format("Game with id: %s could not be found", gameId));
    }

    @Override
    public int getNextGameId() {
        this.idCounter++;
        return this.idCounter;
    }

    @Override
    public void deleteGame(int gameId) {
        Game game = findGame(gameId);
        this.games.remove(game);
    }
}
