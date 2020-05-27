package nl.jonathandegier.lingogame.infrastructure.api.v1;

import nl.jonathandegier.lingogame.application.HighScoreService;
import nl.jonathandegier.lingogame.domain.score.Score;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HighscoreController {

    private static final String URL_PREFIX = "/api/v1";

    private HighScoreService highScoreService;

    public HighscoreController(HighScoreService highScoreService) {
        this.highScoreService = highScoreService;
    }

    @GetMapping(URL_PREFIX + "/scores")
    public List<Score> getScores() {
        return this.highScoreService.getHighscores();
    }
}
