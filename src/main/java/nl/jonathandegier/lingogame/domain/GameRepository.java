package nl.jonathandegier.lingogame.domain;

public interface GameRepository {

    void store(Game game);
    Game findGame(int gameId);
    int getNextGameId();
    void deleteGame(int gameId);
}
