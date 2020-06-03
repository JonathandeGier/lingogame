package nl.jonathandegier.lingogame.domain.exceptions;

public class NoRoundStartedException extends RuntimeException {
    public NoRoundStartedException(String message) {
        super(message);
    }
}
