package app.services;

import app.entities.User;
import app.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesImpl implements Services {
    @Autowired
    private UserRepository repository;

    @Override
    public User add(User user) {
        return repository.save(user);
    }

    @Override
    public User get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Iterable<User> getAll() {
        return repository.findAll();
    }
}
