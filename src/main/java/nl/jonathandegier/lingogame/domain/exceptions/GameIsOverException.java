package nl.jonathandegier.lingogame.domain.exceptions;

public class GameIsOverException extends RuntimeException {
    public GameIsOverException(String message) {
        super(message);
    }
}
