package com.epam.esm.service;

import com.epam.esm.model.Tag;
import java.util.List;

public interface TagService {
    Tag create(Tag tag) throws ReflectiveOperationException;

    Tag update(Tag tag) throws ReflectiveOperationException;

    void delete(Long id);

    Tag get(Long id);

    List<Tag> getAll();
}
