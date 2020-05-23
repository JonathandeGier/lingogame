package nl.jonathandegier.lingogame.infrastructure.database.words.postgres;

import nl.jonathandegier.lingogame.domain.WordRepository;
import nl.jonathandegier.lingogame.infrastructure.database.words.dto.WordDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Random;

@Transactional
public class PostgresWordRepository implements WordRepository {

    private EntityManager entityManager;

    public PostgresWordRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String randomWord(int length) {
        Query countQuery = entityManager.createNativeQuery("select count(*) from words where length = ?");
        countQuery.setParameter(1, length);
        BigInteger count = (BigInteger) countQuery.getSingleResult();

        Random random = new Random();
        int number = random.nextInt(count.intValue());

        Query selectQuery = entityManager.createNativeQuery("select * from words where length = ?", WordDTO.class);
        selectQuery.setParameter(1, length);
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);

        WordDTO word =  (WordDTO) selectQuery.getSingleResult();

        return word.getWord();
    }

    @Override
    public boolean validWord(String word) {
        Query query = entityManager.createNativeQuery("select count(*) from words where word = ?");
        query.setParameter(1, word);
        BigInteger count = (BigInteger) query.getSingleResult();

        return count.intValue() != 0;
    }
}
