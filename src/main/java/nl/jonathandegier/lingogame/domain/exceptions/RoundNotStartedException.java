package nl.jonathandegier.lingogame.domain.exceptions;

public class RoundNotStartedException extends RuntimeException {
    public RoundNotStartedException(String message) {
        super(message);
    }
}
