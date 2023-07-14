package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag create(Tag tag) throws ReflectiveOperationException;

    int update(Tag tag) throws ReflectiveOperationException;

    void delete(Long id);

    Optional<Tag> get(Long id);

    List<Tag> getAll();
}
