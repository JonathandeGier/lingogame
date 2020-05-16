package nl.jonathandegier.lingogame.domain;

public interface WordRepository {

    String randomWord(int length);
    boolean validWord(String word);
}
