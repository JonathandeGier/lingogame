package nl.jonathandegier.lingogame.infrastructure.database.game.postgres;

import nl.jonathandegier.lingogame.domain.ScoreRepository;
import nl.jonathandegier.lingogame.domain.score.Score;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PostgresScoreRepository implements ScoreRepository {

    private EntityManager entityManager;

    public PostgresScoreRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Score> getTopPage(int page, int perPage) {
        return null;
    }

    @Override
    public void storeScore(Score score) {

    }
}
