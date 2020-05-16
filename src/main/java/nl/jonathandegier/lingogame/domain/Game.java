package nl.jonathandegier.lingogame.domain;

import nl.jonathandegier.lingogame.domain.feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int id;
    private List<Round> completedRounds;
    private Round currentRound;

    public Game(int id) {
        this.id = id;
        completedRounds = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public Feedback newRound(Round round) {
        if (this.currentRound != null) {
            this.completedRounds.add(this.currentRound);
        }

        this.currentRound = round;

        return round.startRound();
    }

    public int nextRoundWordLength() {
        return 5;
    }

    public Feedback guess(String guess) {
        return currentRound.guess(guess);
    }

    public int calculateScore() {
        int sum = 0;

        for (Round r : completedRounds) {
            sum += r.calculateScore();
        }

        return sum;
    }
}
