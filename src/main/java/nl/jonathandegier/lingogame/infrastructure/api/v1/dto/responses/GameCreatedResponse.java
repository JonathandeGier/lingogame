package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

public class GameCreatedResponse {
    public int gameId;

    public GameCreatedResponse(int gameId) {
        this.gameId = gameId;
    }
}
