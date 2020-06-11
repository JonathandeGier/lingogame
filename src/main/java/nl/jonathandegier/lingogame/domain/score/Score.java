package nl.jonathandegier.lingogame.domain.score;

public class Score {

    private String player;
    private int points;

    public Score(String player, int points) {
        this.player = player;
        this.points = points;
    }


    public String getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }
}
