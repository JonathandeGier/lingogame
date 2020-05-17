package nl.jonathandegier.lingogame.application;

import nl.jonathandegier.lingogame.domain.Game;
import nl.jonathandegier.lingogame.domain.GameRepository;
import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;

public class GameService {

    private WordRepository wordRepository;
    private ScoreRepository scoreRepository;
    private GameRepository gameRepository;

    public GameService(WordRepository wordRepository, ScoreRepository scoreRepository, GameRepository gameRepository) {
        this.wordRepository = wordRepository;
        this.scoreRepository = scoreRepository;
        this.gameRepository = gameRepository;
    }

    public int startGame() {
        Game game = new Game(this.gameRepository.getNextGameId(), this.wordRepository);
        this.gameRepository.store(game);
        return game.getId();
    }

    public Feedback startRound(int gameId) {
        Game game = this.gameRepository.findGame(gameId);
        return game.newRound();
    }

    public Feedback guess(int gameId, String guess) {
        Game game = this.gameRepository.findGame(gameId);
        return game.guess(guess);
    }

    public void saveScore(int gameId, String player) {
        Game game = this.gameRepository.findGame(gameId);

        int score = game.calculateScore();

        this.scoreRepository.storeScore(new Score(player, score));
        this.gameRepository.deleteGame(gameId);
    }

    public void endGame(int gameId) {
        this.gameRepository.deleteGame(gameId);
    }
}
