package com.epam.esm.repository;

import com.epam.esm.model.Tag;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag create(Tag tag) throws ReflectiveOperationException;

    int update(Tag tag) throws ReflectiveOperationException;

    void delete(Object id);

    Optional<Tag> get(Object id);

    List<Tag> getAll();

    Optional<Tag> getByName(String name);

    List<Tag> getByNames(List<String> names);

    List<Tag> getTagsByGiftCertificateId(BigInteger giftCertificateId);

    void deleteTagsByGiftCertificateId(BigInteger giftCertificateID);
}
