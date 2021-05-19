package app.controllers;

import app.entities.Answer;
import app.entities.Question;
import app.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getQuestionsWithMapAnswerFromChapter(@PathVariable final String name) {
        final Map<String, List<Answer>> quiz = new LinkedHashMap<>();
        final List<Question> questions = StreamSupport
                .stream(services.getQuestionRepository().findAll().spliterator(), false)
                .filter(el -> el.getChapter().getTitle().equals(name))
                .collect(Collectors.toList());
        for (Question question : questions) {
            quiz.put(question.getText(),
                    StreamSupport.stream(services.getAnswerRepository().findAll().spliterator(), false)
                            .filter(el -> el.getQuestion().equals(question)).collect(Collectors.toList()));
            if (quiz.get(question.getText()) == null || quiz.get(question.getText()).size() < 4) {
                /* TODO To be removed - just as example */
                addRandomAnswers(question, quiz.get(question.getText()));
            }
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    public void addRandomAnswers(final Question question, final List<Answer> answers) {
        int i = 0;
        while (answers.size() < 4) {
            final Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setStatus(false);
            answer.setText("answer" + i);
            answers.add(answer);
            i++;
        }
    }
}
