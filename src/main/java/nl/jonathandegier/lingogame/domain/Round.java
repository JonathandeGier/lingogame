package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.feedback.Feedback;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackBuilder;
import nl.jonathandegier.lingogame.domain.feedback.FeedbackExplaination;

import java.time.Instant;

public class Round {

    private WordRepository wordRepository;

    private String word;
    private int guesses;
    private Instant lastGuess;
    private boolean correct;

    public Round(WordRepository wordRepository, int length) {
        this.wordRepository = wordRepository;

        this.word = this.wordRepository.randomWord(length);
        this.guesses = 0;
        this.correct = false;
    }

    public Feedback startRound() {
        this.lastGuess = Instant.now();
        return new FeedbackBuilder()
                .word(this.word)
                .guess(Character.toString(this.word.charAt(0)))
                .explaination(FeedbackExplaination.GOOD_GUESS)
                .build();
    }

    public Feedback guess(String guess) {
        FeedbackBuilder builder = new FeedbackBuilder()
                .word(this.word)
                .guess(guess)
                .explaination(FeedbackExplaination.GOOD_GUESS);

        if (this.word.equals(guess)) {
            builder.explaination(FeedbackExplaination.CORRECT);
        }

        if (!inTime()) {
            builder.explaination(FeedbackExplaination.OUT_OF_TIME);
        }

        validateGuess(guess, builder);

        if (builder.getExplaination() == FeedbackExplaination.CORRECT) {
            this.correct = true;
        }

        this.guesses++;
        this.lastGuess = Instant.now();

        return builder.build();
    }

    public int calculateScore() {
        return 50 - (guesses * 10);
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
