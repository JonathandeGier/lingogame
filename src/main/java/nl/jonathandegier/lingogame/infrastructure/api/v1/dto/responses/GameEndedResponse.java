package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

public class GameEndedResponse {
    private int status;

    public GameEndedResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
