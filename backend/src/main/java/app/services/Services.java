package app.services;

import app.entities.User;
import app.persistance.ScoreRepository;
import app.persistance.UserRepository;
import org.springframework.stereotype.Service;

@Service
public interface Services {
    UserRepository getUserRepository();

    ScoreRepository getScoreRepository();
}
