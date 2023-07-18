package com.epam.esm.service.impl;

import com.epam.esm.exception.DataProcessingException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate)
            throws ReflectiveOperationException {
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        GiftCertificate giftCertificateFromDb = giftCertificateRepository.create(giftCertificate);
        giftCertificateRepository.addTags(giftCertificate);
        return giftCertificateFromDb;
    }

    @Transactional
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate)
            throws ReflectiveOperationException {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        if (giftCertificate.getTags() != null) {
            giftCertificateRepository.addTags(giftCertificate);
        }
        if (giftCertificateRepository.update(giftCertificate) <= 0) {
            throw new DataProcessingException("Can't update gift certificate " + giftCertificate);
        }
        return get(giftCertificate.getId());
    }

    @Transactional
    @Override
    public void delete(BigInteger id) {
        tagRepository.deleteTagsByGiftCertificateId(id);
        giftCertificateRepository.delete(id);
    }

    @Transactional
    @Override
    public GiftCertificate get(BigInteger id) {
        GiftCertificate giftCertificate = giftCertificateRepository.get(id).orElseThrow(
                () -> new DataProcessingException("Can't get gift certificate with id " + id));
        giftCertificate.setTags(tagRepository.getTagsByGiftCertificateId(id));
        return giftCertificate;
    }

    @Transactional
    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> allCertificate = giftCertificateRepository.getAll();
        allCertificate.forEach(c -> c.setTags(tagRepository.getTagsByGiftCertificateId(c.getId())));
        return allCertificate;
    }

    @Override
    public List<GiftCertificate> getAllByTag(String tagName) {
        List<GiftCertificate> allCertificateByTag = giftCertificateRepository.getAllByTag(tagName);
        allCertificateByTag.forEach(c -> c.setTags(tagRepository.getTagsByGiftCertificateId(c.getId())));
        return allCertificateByTag;
    }
}
