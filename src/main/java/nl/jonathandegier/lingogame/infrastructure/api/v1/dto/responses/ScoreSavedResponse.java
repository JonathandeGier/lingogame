package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

import nl.jonathandegier.lingogame.domain.score.Score;

public class ScoreSavedResponse {

    private int status;
    private Score finalScore;

    public ScoreSavedResponse(int status, Score finalScore) {
        this.status = status;
        this.finalScore = finalScore;
    }

    public int getStatus() {
        return status;
    }

    public Score getFinalScore() {
        return finalScore;
    }
}
