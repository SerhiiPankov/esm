package com.epam.esm.mapper.impl;

import com.epam.esm.model.Tag;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getObject("id", BigInteger.class));
        tag.setName(rs.getNString("name"));
        return tag;
    }
}
