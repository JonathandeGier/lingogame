package nl.jonathandegier.lingogame.infrastructure.api.v1;

import nl.jonathandegier.lingogame.application.GameService;
import nl.jonathandegier.lingogame.application.exceptions.GameNotFoundException;
import nl.jonathandegier.lingogame.domain.exceptions.GameIsOverException;
import nl.jonathandegier.lingogame.domain.exceptions.RoundNotStartedException;
import nl.jonathandegier.lingogame.domain.exceptions.UncompletedRoundException;
import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.errors.ErrorBody;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.errors.ErrorType;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.GuessRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests.SaveScoreRequest;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses.GameCreatedResponse;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses.GameEndedResponse;
import nl.jonathandegier.lingogame.infrastructure.api.v1.dto.responses.ScoreSavedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class GameController {

    private static final String URL_PREFIX = "/api/v1";

    private GameService service;

    public GameController(GameService gameService) {
        this.service = gameService;
    }

    @PostMapping(URL_PREFIX + "/game")
    public GameCreatedResponse startGame() {
        int gameId = this.service.startGame();
        return new GameCreatedResponse(gameId);
    }


    @PostMapping(URL_PREFIX + "/game/{gameId}/round")
    public Feedback startRound(@PathVariable int gameId) {
        return this.service.startRound(gameId);
    }


    @PostMapping(URL_PREFIX + "/game/{gameId}/guess")
    public Feedback guess(@PathVariable int gameId, @RequestBody @Valid GuessRequest guessRequest) {
        return this.service.guess(gameId, guessRequest.guess);
    }


    @GetMapping(URL_PREFIX + "/game/{gameId}/score")
    public Score getScore(@PathVariable int gameId) {
        return new Score("", this.service.getScore(gameId));
    }


    @PostMapping(URL_PREFIX + "/game/{gameId}/score")
    public ScoreSavedResponse saveScore(@PathVariable int gameId, @RequestBody @Valid SaveScoreRequest saveScoreRequest) {
        Score score = this.service.saveScore(gameId, saveScoreRequest.name);
        return new ScoreSavedResponse(200, score);
    }


    @DeleteMapping(URL_PREFIX + "/game/{gameId}")
    public GameEndedResponse endGame(@PathVariable int gameId) {
        this.service.endGame(gameId);
        return new GameEndedResponse(200);
    }


    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity handleGameNotFoundException(GameNotFoundException e) {
        return new ResponseEntity(new ErrorBody(HttpStatus.NOT_FOUND, e.getMessage(), ErrorType.GAME_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameIsOverException.class)
    public ResponseEntity handleGameIsOverException(GameIsOverException e) {
        return new ResponseEntity(new ErrorBody(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), ErrorType.GAME_ALREADY_OVER), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(RoundNotStartedException.class)
    public ResponseEntity handleRoundNotStartedException(RoundNotStartedException e) {
        return new ResponseEntity(new ErrorBody(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), ErrorType.ROUND_NOT_STARTED), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UncompletedRoundException.class)
    public ResponseEntity handleUncompletedRoundException(UncompletedRoundException e) {
        return new ResponseEntity(new ErrorBody(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), ErrorType.UNCOMPLETED_ROUND), HttpStatus.NOT_ACCEPTABLE);
    }
}
