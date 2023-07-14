package com.epam.esm.repository.extractor;

import com.epam.esm.model.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class TagResultSetExtractor implements ResultSetExtractor<Tag> {
    @Override
    public Tag extractData(ResultSet rs) throws SQLException, DataAccessException {
        Tag tag = new Tag();
        if (rs.next()) {
            tag.setId(rs.getObject("id", Long.class));
            tag.setName(rs.getNString("name"));
        }
        return tag;
    }
}
