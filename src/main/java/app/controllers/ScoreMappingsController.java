package app.controllers;

import app.entities.Score;
import app.entities.User;
import app.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/rankings")
public class ScoreMappingsController {
    private static final Logger logger = LoggerFactory.getLogger(ScoreMappingsController.class);
    @Autowired
    private Services services;

    @RequestMapping(method = RequestMethod.GET, path = "/rankings/getAll/{order}")
    public ResponseEntity<?> getAllRankingsInSpecifiedOrderByScore(@PathVariable String order) {
        try {
            final Iterable<Score> all = services.getScoreRepository().findAll();
            final List<Score> scores;
            if (order.equals("ascending")) {
                scores = StreamSupport.stream(all.spliterator(), false)
                        .sorted(Comparator.comparing(Score::getScore))
                        .collect(Collectors.toList());
            } else if (order.equals("descending")) {
                scores = StreamSupport.stream(all.spliterator(), false)
                        .sorted((el1, el2) -> Integer.compare(el2.getScore(), el1.getScore()))
                        .collect(Collectors.toList());
            } else {
                scores = StreamSupport.stream(all.spliterator(), false)
                        .collect(Collectors.toList());
            }
            return new ResponseEntity<>(scores, HttpStatus.ACCEPTED);
        } catch (final Exception ex) {
            logger.error("getAllRankingsInSpecifiedOrderByScore -> failed with error: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/rankings/")
    public ResponseEntity<?> registerScore(@RequestBody final Map<String, Object> requestBody) {
        try {
            if (requestBody.containsKey("score") && requestBody.containsKey("user")) {
                final Integer grade = (Integer) requestBody.get("score");
                final User user = (User) requestBody.get("user");
                final Score score = new Score();
                score.setScore(grade);
                score.setUser(user);
                services.getScoreRepository().save(score);
            }
            return new ResponseEntity<>("score and user need to be present in the value map", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception ex) {
            logger.error("registerScore -> failed with error: {}", ex.getMessage());
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
