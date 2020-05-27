package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.score.Score;

import java.util.List;

public interface ScoreRepository {

    List<Score> getScores();
    void storeScore(Score score);
}
