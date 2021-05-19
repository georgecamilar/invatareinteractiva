package app.services;

import app.entities.User;
import app.persistance.ChapterRepository;
import app.persistance.QuestionRepository;
import app.persistance.ScoreRepository;
import app.persistance.UserRepository;
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
}
