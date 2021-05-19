package app.services;

import app.entities.User;
import app.persistance.*;
import org.springframework.stereotype.Service;

@Service
public interface Services {
    UserRepository getUserRepository();

    ScoreRepository getScoreRepository();

    ChapterRepository getChapterRepository();

    QuestionRepository getQuestionRepository();

    AnswerRepository getAnswerRepository();
}
