package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    GiftCertificate create(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    int update(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    void delete(Long id);

    Optional<GiftCertificate> get(Long id);

    List<GiftCertificate> getAll();
}
