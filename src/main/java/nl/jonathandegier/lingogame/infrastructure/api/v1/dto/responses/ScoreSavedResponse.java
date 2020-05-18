package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses;

import nl.jonathandegier.lingogame.domain.score.Score;

public class ScoreSavedResponse {

    public int status;
    public Score finalScore;

    public ScoreSavedResponse(int status, Score finalScore) {
        this.status = status;
        this.finalScore = finalScore;
    }
}
