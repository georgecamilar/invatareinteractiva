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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/rankings")
@CrossOrigin(origins = "http://localhost:4200")
public class ScoreMappingsController {
    private static final Logger logger = LoggerFactory.getLogger(ScoreMappingsController.class);
    @Autowired
    private Services services;

    @RequestMapping(method = RequestMethod.GET, path = "/getAll/{order}")
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> registerScore(@RequestBody final ScoreTransferDto transfer) {
        try {

            var scoreOptional = StreamSupport.stream(services.getScoreRepository().findAll().spliterator(),false)
                    .filter(score1 -> score1.getUser().getUsername().equals(transfer.user.getUsername())).findFirst();

            if (scoreOptional.isPresent()){
                final Score score1 = scoreOptional.get();
                score1.setScore(score1.getScore()+ transfer.score);
                services.getScoreRepository().save(score1);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                final Score score = new Score();
                score.setScore(transfer.score);
                final Optional<User> first = StreamSupport.stream(services.getUserRepository().findAll().spliterator(), false)
                        .filter(el -> el.getUsername().equals(transfer.user.getUsername()))
                        .filter(el -> el.getPassword().equals(transfer.user.getPassword()))
                        .findFirst();
                first.ifPresent(score::setUser);
                services.getScoreRepository().save(score);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error("registerScore -> failed with error: {}", ex.getMessage());
        }
        return new ResponseEntity<>("Can't register score", HttpStatus.BAD_REQUEST);
    }

    static class ScoreTransferDto {
        private Integer score;
        private User user;
        public Integer getScore() {
            return score;
        }
        public void setScore(Integer score) {
            this.score = score;
        }
        public User getUser() {
            return user;
        }
        public void setUser(User user) {
            this.user = user;
        }
    }
}
