package app.persistance;

import app.entities.Score;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Integer> {
}
