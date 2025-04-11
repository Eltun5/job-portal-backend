package org.ea.jobportalbackend.controller;

import lombok.extern.java.Log;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Tag> create(@RequestBody Tag tag){
        return ResponseEntity.ok(service.create(tag));
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getAll(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id,
                                      @RequestBody Tag tag){
        return ResponseEntity.ok(service.update(id, tag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
