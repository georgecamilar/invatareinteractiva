package app.controllers;

import app.entities.User;
import app.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.BiPredicate;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "http://locahost:8080/")
@RequestMapping(path = "/app")
public class MappingController {
    private static final Logger logger = LoggerFactory.getLogger(MappingController.class);

    @Autowired
    private Services services;

    @RequestMapping(path = "/echo")
    public String echo() {
        return "Working";
    }

    @RequestMapping(path = "/user/config/add", method = RequestMethod.POST)
    public void saveUser(@RequestBody User user) {
        services.add(user);
    }

    @RequestMapping(path = "/user/config/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            Integer givenId = Integer.parseInt(id);
            return new ResponseEntity<>(services.get(givenId), HttpStatus.OK);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage());
            logger.error("{} is not a number", id);
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        if (StreamSupport.stream(services.getAll().spliterator(), false)
                .filter(el -> userComparator.test(el, user))
                .count() == 1)
            return ResponseEntity.status(200).build();
        return ResponseEntity.status(403).build();
    }

    private final BiPredicate<User, User> userComparator = (streamElement, comparator) ->
            streamElement.getUsername().equals(comparator.getUsername())
                    && streamElement.getPassword().equals(comparator.getPassword());
}
