package app.services;

import app.entities.User;
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

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ScoreRepository getScoreRepository() {
        return scoreRepository;
    }
}
