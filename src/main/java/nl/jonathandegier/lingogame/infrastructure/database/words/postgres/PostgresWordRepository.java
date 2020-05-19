package nl.jonathandegier.lingogame.infrastructure.database.words.postgres;

import nl.jonathandegier.lingogame.domain.WordRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class PostgresWordRepository implements WordRepository {

    private EntityManager entityManager;

    public PostgresWordRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String randomWord(int length) {
        return null;
    }

    @Override
    public boolean validWord(String word) {
        return false;
    }
}
