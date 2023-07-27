package com.epam.esm.repository.extractor;

import com.epam.esm.model.GiftCertificate;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateResultSetExtractor implements ResultSetExtractor<GiftCertificate> {
    @Override
    public GiftCertificate extractData(ResultSet rs) throws SQLException, DataAccessException {
        GiftCertificate giftCertificate = new GiftCertificate();
        if (rs.next()) {
            giftCertificate.setId(rs.getObject("id", BigInteger.class));
            giftCertificate.setName(rs.getNString("name"));
            giftCertificate.setDescription(rs.getNString("description"));
            giftCertificate.setPrice(rs.getBigDecimal("price"));
            giftCertificate.setDuration(rs.getInt("duration"));
            giftCertificate.setCreateDate(rs.getObject("createDate", LocalDateTime.class));
            giftCertificate.setLastUpdateDate(rs.getObject("lastUpdateDate", LocalDateTime.class));
        }
        return giftCertificate;
    }
}
