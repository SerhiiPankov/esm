package com.epam.esm.service.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate)
            throws ReflectiveOperationException {
        return giftCertificateRepository.create(giftCertificate);
    }

    @Override
    public int update(GiftCertificate giftCertificate) throws ReflectiveOperationException {
        return giftCertificateRepository.update(giftCertificate);
    }

    @Override
    public void delete(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Override
    public Optional<GiftCertificate> get(Long id) {
        return giftCertificateRepository.get(id);
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateRepository.getAll();
    }
}
