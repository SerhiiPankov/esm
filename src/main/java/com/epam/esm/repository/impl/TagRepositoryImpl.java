package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.TagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        super(jdbcTemplate, Tag.class, rowMapper);
    }
}
