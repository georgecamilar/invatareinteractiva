package app.services;

import app.entities.User;
import app.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesImpl implements Services {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public ScoreRepository getScoreRepository() {
        return scoreRepository;
    }

    @Override
    public ChapterRepository getChapterRepository() {
        return chapterRepository;
    }

    @Override
    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public AnswerRepository getAnswerRepository() {
        return answerRepository;
    }
}
