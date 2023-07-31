package com.epam.esm.repository;

import com.epam.esm.config.TestDbConfiguration;
import com.epam.esm.model.Tag;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDbConfiguration.class)
class GiftCertificateRepositoryImplTest {
    private static final String DB_NAME = "`esm`";

    private final JdbcTemplate jdbcTemplate;
    private final TagRepository tagRepository;

    public GiftCertificateRepositoryImplTest(JdbcTemplate jdbcTemplate, TagRepository tagRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRepository = tagRepository;
    }

    @BeforeEach
    void setUp() throws IOException {
//        fillDataBase(new String[] {"init_db.sql"});
    }

    @AfterEach
    void tearDown() {
        cleanDataBase();
    }

    @Test
    void addTags() {
        Tag tag = new Tag();
        tag.setName("QWERTY");
        tagRepository.create(tag);

        System.out.println(tagRepository.get(new BigInteger("1")));
    }

    @Test
    void getAllByParameters() {
    }

    private void fillDataBase(String[] initList) throws IOException {
        for (String x : initList) {
            jdbcTemplate.update(IOUtils.resourceToString("/resources/" + x,
                    StandardCharsets.UTF_8));
        }
    }

    public void cleanDataBase() {
        jdbcTemplate.update("DROP database " + DB_NAME);
    }
}