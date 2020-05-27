package nl.jonathandegier.lingogame.infrastructure.database.game.postgres;

import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.score.Score;
import nl.jonathandegier.lingogame.infrastructure.database.game.dto.ScoreDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class PostgresScoreRepository implements ScoreRepository {

    private EntityManager entityManager;

    public PostgresScoreRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Score> getScores() {
        Query query = this.entityManager.createNativeQuery("select * from scores order by score desc", ScoreDTO.class);

        List<ScoreDTO> scoresDtos = query.getResultList();

        List<Score> scores = new ArrayList<>();
        for (ScoreDTO dto : scoresDtos) {
            scores.add(new Score(dto.getName(), dto.getScore()));
        }

        return scores;
    }

    @Override
    public void storeScore(Score score) {
        ScoreDTO scoreDTO = new ScoreDTO(score);
        this.entityManager.persist(scoreDTO);
    }
}
