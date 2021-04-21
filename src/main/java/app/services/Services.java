package app.services;

import app.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface Services {
    User add(final User user);
    User get(final Integer id);
    Iterable<User> getAll();
}
