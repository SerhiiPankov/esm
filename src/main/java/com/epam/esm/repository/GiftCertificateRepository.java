package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    GiftCertificate create(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    int update(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    void delete(Object id);

    Optional<GiftCertificate> get(Object id);

    List<GiftCertificate> getAll();

    void addTags(GiftCertificate giftCertificate);

    List<GiftCertificate> getAllByTag(String tagName);
}
