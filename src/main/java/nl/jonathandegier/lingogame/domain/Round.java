package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackBuilder;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackExplaination;

import java.time.Instant;

public class Round {

    private static final int MAX_GUESSES = 5;

    private WordRepository wordRepository;

    private String word;
    private int guesses;
    private Instant lastGuess;
    private boolean complete;

    public Round(WordRepository wordRepository, int length) {
        this.wordRepository = wordRepository;
        this.word = this.wordRepository.randomWord(length);
        this.guesses = 0;
        this.complete = false;
    }

    public Feedback startRound() {
        this.lastGuess = Instant.now();
        return new FeedbackBuilder()
                .word(this.word)
                .guess(Character.toString(this.word.charAt(0)), MAX_GUESSES, MAX_GUESSES)
                .explaination(FeedbackExplaination.GOOD_GUESS)
                .build();
    }

    public Feedback guess(String guess) {
        if (this.guesses == MAX_GUESSES) {
            throw new IllegalArgumentException("This game is already over");
        }

        if (this.lastGuess == null) {
            throw new IllegalArgumentException("Round not started yet");
        }

        this.guesses++;

        FeedbackBuilder builder = new FeedbackBuilder()
                .word(this.word)
                .guess(guess, MAX_GUESSES, MAX_GUESSES - this.guesses)
                .explaination(FeedbackExplaination.GOOD_GUESS);

        if (this.word.equals(guess)) {
            builder.explaination(FeedbackExplaination.CORRECT);
        }

        if (!this.inTime()) {
            builder.explaination(FeedbackExplaination.OUT_OF_TIME);
        }

        this.validateGuess(guess, builder);

        if (this.guesses == MAX_GUESSES && builder.getExplaination() != FeedbackExplaination.CORRECT) {
            builder.explaination(FeedbackExplaination.GAME_OVER);
            this.complete = true;
        }

        if (builder.getExplaination() == FeedbackExplaination.CORRECT) {
            this.complete = true;
        }

        this.lastGuess = Instant.now();

        return builder.build();
    }

    public int calculateScore() {
        return ((MAX_GUESSES + 1) * 10) - (guesses * 10);
    }

    public int getWordLength() {
        return this.word.length();
    }

    public boolean roundCompleted() {
        return this.complete;
    }

    private void validateGuess(String guess, FeedbackBuilder builder) {

        if (!wordRepository.validWord(guess)) {
            builder.explaination(FeedbackExplaination.WORD_DOES_NOT_EXIST);
        }

        if (guess.length() < this.word.length()) {
            builder.explaination(FeedbackExplaination.WORD_TOO_SHORT);
        } else if (guess.length() > this.word.length()) {
            builder.explaination(FeedbackExplaination.WORD_TOO_LONG);
        }
    }

    private boolean inTime() {
        return Instant.now().minusSeconds(10).isBefore(this.lastGuess);
    }
}
