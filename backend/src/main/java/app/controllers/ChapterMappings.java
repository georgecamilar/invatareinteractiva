package app.controllers;

import app.entities.Chapter;
import app.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/chapter")
public class ChapterMappings {

    @Autowired
    private Services services;

    @GetMapping

    public ResponseEntity<?> getAllChapter() {
        try {
            return new ResponseEntity<>(StreamSupport.stream(services.getChapterRepository().findAll().spliterator(), false)
                    .map(Chapter::getTitle)
                    .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>("not ok", HttpStatus.FORBIDDEN);
        }
    }
}
