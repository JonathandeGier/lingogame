package nl.jonathandegier.lingogame.application;

import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.score.Score;

import java.util.List;

public class HighScoreService {

    private ScoreRepository scoreRepository;

    public HighScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public List<Score> getHighscores() {
        return scoreRepository.getScores();
    }

    public void saveScore(Score score) {
        this.scoreRepository.storeScore(score);
    }
}
