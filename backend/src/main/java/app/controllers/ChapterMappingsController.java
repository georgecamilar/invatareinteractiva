package app.controllers;

import app.entities.Chapter;
import app.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/chapter")
public class ChapterMappingsController {

    @Autowired
    private Services services;

    @GetMapping
    public ResponseEntity<?> getAllChapter() {
        try {
            return new ResponseEntity<>(StreamSupport.stream(services.getChapterRepository().findAll().spliterator(), false)
                    .map(Chapter::getTitle)
                    .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>("Cannot fetch the data from the database", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<?> postChapter(@RequestBody Chapter chapter) {
        try {
            services.getChapterRepository().save(chapter);
            return new ResponseEntity<>("Saved!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
