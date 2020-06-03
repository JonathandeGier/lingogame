package nl.jonathandegier.lingogame.domain.exceptions;

public class UncompletedRoundException extends RuntimeException {
    public UncompletedRoundException(String message) {
        super(message);
    }
}
