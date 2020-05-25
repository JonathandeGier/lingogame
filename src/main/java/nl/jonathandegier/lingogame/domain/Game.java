package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private WordRepository wordRepository;

    private int id;
    private List<Round> completedRounds;
    private Round currentRound;

    public Game(int id, WordRepository wordRepository) {
        this.wordRepository = wordRepository;
        this.id = id;
        completedRounds = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public Feedback newRound() {
        if (this.currentRound != null) {
            if (!this.currentRound.roundCompleted()) {
                throw new IllegalArgumentException("Uncompleted round");
            }

            this.completedRounds.add(this.currentRound);
        }

        this.currentRound = new Round(wordRepository, nextRoundWordLength());

        return this.currentRound.startRound();
    }

    public Feedback guess(String guess) {
        return currentRound.guess(guess);
    }

    public int calculateScore() {
        int sum = 0;

        for (Round r : completedRounds) {
            sum += r.calculateScore();
        }

        return sum + this.currentRound.calculateScore();
    }

    private int nextRoundWordLength() {
        if (this.currentRound == null) {
            return 5;
        }

        if (this.currentRound.getWordLength() == 7) {
            return 5;
        }

        return this.currentRound.getWordLength() + 1;
    }
}
