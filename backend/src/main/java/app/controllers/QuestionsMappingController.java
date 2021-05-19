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
@CrossOrigin(origins = "http://localhost:4200")
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

        }
        final List<QuestionDto> toSend = new ArrayList<>();
        for (final String key : quiz.keySet()) {
            final List<Answer> validAnswers = quiz.get(key).stream().filter(Answer::isStatus).collect(Collectors.toList());
            final String[] answers = quiz.get(key).stream().map(Answer::getText).toArray(String[]::new);
            toSend.add(new QuestionDto(key, answers, validAnswers.get(0).getText()));
        }
        return new ResponseEntity<>(toSend, HttpStatus.OK);
    }


    static class QuestionDto {
        private String questionText;
        private String[] answers;
        private String correctAnswer;

        public QuestionDto(String questionText, String[] answers, String correctAnswer) {
            this.questionText = questionText;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String[] getAnswers() {
            return answers;
        }

        public void setAnswers(String[] answers) {
            this.answers = answers;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }
    }
}
