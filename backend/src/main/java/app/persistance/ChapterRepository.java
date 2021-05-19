package app.persistance;

import app.entities.Chapter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends CrudRepository<Chapter, Integer> {
}
