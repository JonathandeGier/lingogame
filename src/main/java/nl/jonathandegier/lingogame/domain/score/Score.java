package nl.jonathandegier.lingogame.domain.score;

public class Score {

    private String player;
    private int score;

    public Score(String player, int score) {
        this.player = player;
        this.score = score;
    }


    public String getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }
}
