package app.controllers;

import app.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/questions")
public class QuestionsMappingController {

    @Autowired
    private Services services;

    @RequestMapping(method = RequestMethod.GET, path = "/byId/{chapterId}")
    public ResponseEntity<?> getQuestionsFromChapter(@PathVariable String chapterId) {
        try {
            return new ResponseEntity<>(
                    StreamSupport
                            .stream(services.getQuestionRepository().findAll().spliterator(), false)
                            .filter(el -> el.getChapter().getChapterId().equals(Integer.parseInt(chapterId)))
                            .collect(Collectors.toList())
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            return new ResponseEntity<>("Cannot get Chapter questions", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/byName/{name}")
    public ResponseEntity<?> getQuestionsFromChapterByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(
                    StreamSupport
                            .stream(services.getQuestionRepository().findAll().spliterator(), false)
                            .filter(el -> el.getChapter().getTitle().equals(name))
                            .collect(Collectors.toList())
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            return new ResponseEntity<>("Cannot get Chapter questions", HttpStatus.BAD_REQUEST);
        }
    }
}
