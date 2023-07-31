package com.epam.esm.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Log4j2
@Configuration
public class TestDbConfiguration{
    private static final String CONNECTION_URL = "jdbc:hsqldb:mem:test";

    @Bean
    public DataSource getMysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(CONNECTION_URL);
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getMysqlDataSource());
    }
}
