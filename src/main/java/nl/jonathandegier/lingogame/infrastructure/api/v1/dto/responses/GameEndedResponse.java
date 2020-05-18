package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

public class GameEndedResponse {
    public int status;

    public GameEndedResponse(int status) {
        this.status = status;
    }
}
