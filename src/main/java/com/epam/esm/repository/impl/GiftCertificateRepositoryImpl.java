package com.epam.esm.repository.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.ReflectionUtil;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate>
        implements GiftCertificateRepository {
    private static final int SHIFT = 2;

    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate,
                                         RowMapper<GiftCertificate> rowMapper,
                                         ReflectionUtil<GiftCertificate> reflectionUtil) {
        super(jdbcTemplate, GiftCertificate.class, rowMapper, reflectionUtil);
    }

    @Override
    public void addTags(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTags();
        String sql = "INSERT INTO Tag_GiftCertificate (gift_certificate_id, tag_id) VALUES "
                + tags.stream().map(tag -> "(?, ?)").collect(Collectors.joining(", "))
                + " ON DUPLICATE KEY UPDATE gift_certificate_id = gift_certificate_id";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                preparedStatement.setObject((i * SHIFT) + 1, giftCertificate.getId());
                preparedStatement.setObject((i * SHIFT) + 2, tag.getId());
            }
            return preparedStatement;
        });
    }

    @Override
    public List<GiftCertificate> getAllByParameters(String spec) {
        String sql = "SELECT DISTINCT gc.id AS id, gc.name AS name, gc.description AS description, "
                + "gc.price AS price, gc.duration AS duration, "
                + " gc.createDate AS createDate, gc.lastUpdateDate AS lastUpdateDate"
                + " FROM GiftCertificate gc "
                + " INNER JOIN Tag_GiftCertificate tgc ON gc.id = tgc.gift_certificate_id "
                + " INNER JOIN Tag t ON tgc.tag_id = t.id "
                + spec;
        return jdbcTemplate.query(sql, rowMapper);
    }
}
