package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.ReflectionUtil;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper,
                             ReflectionUtil<Tag> reflectionUtil) {
        super(jdbcTemplate, Tag.class, rowMapper, reflectionUtil);
    }

    @Override
    public List<Tag> getByNames(List<String> names) {
        String sql = "SELECT * FROM Tag "
                + " WHERE name IN "
                + names.stream().collect(Collectors.joining("', '", "('", "')"));
        return jdbcTemplate.query(sql, super.rowMapper);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(BigInteger giftCertificateId) {
        String sql = "SELECT id, name FROM Tag_GiftCertificate tgc"
                + " JOIN Tag t ON tgc.tag_id = t.id"
                + " WHERE tgc.gift_certificate_id = " + giftCertificateId;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteTagsByGiftCertificateId(BigInteger giftCertificateID) {
        String sql = "DELETE FROM Tag_GiftCertificate WHERE gift_certificate_id = "
                + giftCertificateID;
        jdbcTemplate.execute(sql);
    }
}
