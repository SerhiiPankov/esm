package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import java.math.BigInteger;
import java.util.List;

public interface GiftCertificateService {
    GiftCertificate create(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    GiftCertificate update(GiftCertificate giftCertificate) throws ReflectiveOperationException;

    void delete(BigInteger id);

    GiftCertificate get(BigInteger id);

    List<GiftCertificate> getAll();

    List<GiftCertificate> getAllByTag(String tagName);
}
