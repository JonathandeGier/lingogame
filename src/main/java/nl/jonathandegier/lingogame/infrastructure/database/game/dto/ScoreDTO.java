package nl.jonathandegier.lingogame.infrastructure.database.game.dto;

import nl.jonathandegier.lingogame.domain.score.Score;

import javax.persistence.*;

@Entity
@Table(name = "scores")
public class ScoreDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private int score;

    public ScoreDTO() {}
    public ScoreDTO(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public ScoreDTO(long id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
    public ScoreDTO(Score score) {
        this(score.getPlayer(), score.getPoints());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
