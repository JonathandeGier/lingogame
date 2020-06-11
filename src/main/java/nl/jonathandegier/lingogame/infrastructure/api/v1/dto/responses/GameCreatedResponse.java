package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

public class GameCreatedResponse {
    private int gameId;

    public GameCreatedResponse(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
