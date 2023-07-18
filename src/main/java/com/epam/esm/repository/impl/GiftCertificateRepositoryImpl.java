package com.epam.esm.repository.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate>
        implements GiftCertificateRepository {
    private static final int SHIFT = 2;

    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate,
                                         RowMapper<GiftCertificate> rowMapper) {
        super(jdbcTemplate, GiftCertificate.class, rowMapper);
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
    public List<GiftCertificate> getAllByTag(String tagName) {
        String sql = "SELECT * FROM GiftCertificate gc"
                + " JOIN Tag_GiftCertificate tgc ON gc.id = tgc.gift_certificate_id"
                + " JOIN Tag t ON t.id = tgc.tag_id"
                + " WHERE t.name = '" + tagName + "'";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
