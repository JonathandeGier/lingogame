package nl.jonathandegier.lingogame.infrastructure.database.words.dto;

import javax.persistence.*;

@Entity
@Table(schema = "lingowords", name = "words", indexes = {@Index(name = "length_index", columnList = "length")})
public class WordDTO {

    @Id
    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "length", nullable = false)
    private int length;


    public String getWord() {
        return word;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WordDTO) {
            WordDTO other = (WordDTO) obj;

            return this.word.equals(other.word) && this.length == other.length;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.word.hashCode() + this.length;
    }
}
