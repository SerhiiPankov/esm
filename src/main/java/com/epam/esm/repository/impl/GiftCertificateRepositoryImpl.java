package com.epam.esm.repository.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate>
        implements GiftCertificateRepository {

    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate,
                                         RowMapper<GiftCertificate> rowMapper) {
        super(jdbcTemplate, GiftCertificate.class, rowMapper);
    }
}
