package com.epam.esm.service.impl;

import com.epam.esm.exception.DataProcessingException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag create(Tag tag) throws ReflectiveOperationException {
        return tagRepository.create(tag);
    }

    @Transactional
    @Override
    public Tag update(Tag tag) throws ReflectiveOperationException {
        if (tagRepository.update(tag) <= 0) {
            throw new DataProcessingException("Can't update tag " + tag);
        }
        return tagRepository.get(tag.getId()).orElseThrow(
                () -> new DataProcessingException("Can't update tag " + tag));
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }

    @Override
    public Tag get(Long id) {
        return tagRepository.get(id).orElseThrow(
                () -> new DataProcessingException("Can't get tag with id " + id));
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }
}
