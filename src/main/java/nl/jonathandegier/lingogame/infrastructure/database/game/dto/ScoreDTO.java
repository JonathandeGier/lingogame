package nl.jonathandegier.lingogame.infrastructure.database.game.dto;

import javax.persistence.*;

@Entity
@Table(schema = "lingogame", name = "scores")
public class ScoreDTO {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private int score;

    public ScoreDTO() {}
    public ScoreDTO(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public ScoreDTO(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
