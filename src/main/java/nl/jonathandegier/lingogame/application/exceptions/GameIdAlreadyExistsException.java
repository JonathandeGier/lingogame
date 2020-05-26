package nl.jonathandegier.lingogame.application.exceptions;

public class GameIdAlreadyExistsException extends RuntimeException {

    public GameIdAlreadyExistsException(String message) {
        super(message);
    }
}
