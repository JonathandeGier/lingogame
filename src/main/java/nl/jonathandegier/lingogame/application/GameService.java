package nl.jonathandegier.lingogame.application;

import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    private WordRepository wordRepository;
    private ScoreRepository scoreRepository;

    private int nextGameId;

    private List<Game> currentGames;

    public GameService(WordRepository wordRepository, ScoreRepository scoreRepository) {
        this.wordRepository = wordRepository;
        this.scoreRepository = scoreRepository;
        this.nextGameId = 1;
        this.currentGames = new ArrayList<>();
    }

    public int startGame() {
        Game game = new Game(0, this.wordRepository);
        currentGames.add(game);
        return game.getId();
    }

    public Feedback startRound(int gameId) {
        Game game = findGame(gameId);
        return game.newRound();
    }

    public Feedback guess(int gameId, String guess) {
        Game game = findGame(gameId);
        return game.guess(guess);
    }

    public void saveScore(int gameId, String player) {
        Game game = findGame(gameId);

        int score = game.calculateScore();

        this.scoreRepository.storeScore(new Score(player, score));
    }

    private Game findGame(int gameId) {
        for (Game g : currentGames) {
            if (g.getId() == gameId) {
                return g;
            }
        }

        throw new GameNotFoundException(String.format("Game with id: %s could not be found!", gameId));
    }

    private int getNextGameId() {
        this.nextGameId++;
        return this.nextGameId;
    }
}
