package app.controllers;

import app.entities.User;
import app.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "https://locahost:8080/")
@RequestMapping(path = "/app/users")
public class UserMappingController {
    private static final Logger logger = LoggerFactory.getLogger(UserMappingController.class);
    private final List<User> loggedIn = new ArrayList<>();

    @Autowired
    private Services services;

    @RequestMapping(path = "/echo")
    public String echo() {
        return "Working";
    }

    @RequestMapping(path = "/user/config/add", method = RequestMethod.POST)
    public void saveUser(@RequestBody User user) {
        services.getUserRepository().save(user);
    }

    @RequestMapping(path = "/user/config/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            final Integer givenId = Integer.parseInt(id);
            return new ResponseEntity<>(services.getUserRepository().findById(givenId), HttpStatus.OK);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            logger.error("{} is not a number", id);
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        if (loggedIn.contains(user)) {
            return new ResponseEntity<>("Logged in already", HttpStatus.PERMANENT_REDIRECT);
        }
        if (StreamSupport.stream(services.getUserRepository().findAll().spliterator(), false)
                .filter(el -> userComparator.test(el, user))
                .count() == 1) {
            this.loggedIn.add(user);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(403).build();
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {
            if (body.containsKey("username") && body.containsKey("password")) {
                final String username = body.get("username").toString();
                final String password = body.get("password").toString();
                final long count = StreamSupport.stream(services.getUserRepository().findAll().spliterator(), false)
                        .filter(el -> el.getUsername().equals(username))
                        .filter(el -> el.getPassword().equals(password))
                        .count();
                if (count > 0) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                final User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                services.getUserRepository().save(user);
                return new ResponseEntity<>(HttpStatus.PERMANENT_REDIRECT);
            }
            return new ResponseEntity<>("Cannot register this user", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception ex) {
            return new ResponseEntity<>("Inserted information is wrong", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private final BiPredicate<User, User> userComparator = (streamElement, comparator) ->
            streamElement.getUsername().equals(comparator.getUsername())
                    && streamElement.getPassword().equals(comparator.getPassword());
}
