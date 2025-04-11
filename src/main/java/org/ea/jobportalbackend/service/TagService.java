package org.ea.jobportalbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.ea.jobportalbackend.model.Tag;
import org.ea.jobportalbackend.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TagService {
    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public Tag create(Tag tag){
        repository.save(tag);
        return repository.getTagByName(tag.getName()).orElseThrow(()->{
            log.error("Cannot find a tag with this id.");
            return new NoSuchElementException("Cannot find a tag with this id.");
        });
    }

    public List<Tag> findAll(){
        return repository.findAll();
    }

    public Tag findById(Long id){
        return repository.findById(id).orElseThrow(()->{
            log.error("Cannot find a tag with this id.");
            return new NoSuchElementException("Cannot find a tag with this id.");
        });
    }

    public Tag update(Long id, Tag newTag){
        Tag oldTag = findById(id);

        oldTag.setName(newTag.getName());
        oldTag.setUpdateAt(Instant.now());

        repository.save(oldTag);

        return findById(id);
    }

    public void delete(Long id){
        log.warn("Someone try to delete tag!!!");
        repository.delete(findById(id));
    }

}
