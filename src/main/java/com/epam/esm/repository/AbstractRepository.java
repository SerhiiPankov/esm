package com.epam.esm.repository;

import com.epam.esm.util.ReflectionUtil;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractRepository<T> {
    private static final Collector<CharSequence, ?, String> COLLECTOR =
            Collectors.joining(", ", " (", ") ");
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;
    protected final ReflectionUtil<T> reflectionUtil;
    private final Class<T> clazz;

    public AbstractRepository(JdbcTemplate jdbcTemplate, Class<T> clazz,
                              RowMapper<T> rowMapper, ReflectionUtil<T> reflectionUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.clazz = clazz;
        this.rowMapper = rowMapper;
        this.reflectionUtil = reflectionUtil;
    }

    public T create(T t) {
        Map<String, Object> entityData = reflectionUtil.getEntityData(t, clazz);
        String sql = generateInsertSqlQuery(entityData);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Map.Entry<String, Object> entry : entityData.entrySet()) {
                preparedStatement.setObject(i, entry.getValue());
                i++;
            }
            return preparedStatement;
        },
                keyHolder);
        return reflectionUtil.setId(t,clazz, keyHolder.getKeyAs(
                reflectionUtil.getIdField(t, clazz).getType()));
    }

    public int update(T t) {
        Map<String, Object> entityData = reflectionUtil.getEntityData(t, clazz);
        String sql = generateUpdateSqlQuery(entityData);
        Object id = reflectionUtil.getId(t, clazz);
        return jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int i = 1;
            for (Map.Entry<String, Object> entry : entityData.entrySet()) {
                preparedStatement.setObject(i, entry.getValue());
                i++;
            }
            preparedStatement.setObject(i, id);
            return preparedStatement;
        });
    }

    public void delete(Object id) {
        String sql = "DELETE FROM " + clazz.getSimpleName()
                + " WHERE id = " + id;
        jdbcTemplate.execute(sql);
    }

    public Optional<T> get(Object id) {
        String sql = "SELECT * FROM " + clazz.getSimpleName()
                + " WHERE id = " + id;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper));
    }

    public List<T> getAll() {
        String sql = "SELECT * FROM " + clazz.getSimpleName();
        return jdbcTemplate.query(sql, rowMapper);
    }

    private String generateInsertSqlQuery(Map<String, Object> entityData) {
        return "INSERT INTO " + clazz.getSimpleName()
                + entityData.keySet().stream().collect(COLLECTOR)
                + "VALUES "
                + entityData.keySet().stream()
                        .map(value -> "?").collect(COLLECTOR);
    }

    private String generateUpdateSqlQuery(Map<String, Object> entityData) {
        return "UPDATE " + clazz.getSimpleName() + " SET "
                + entityData.keySet().stream()
                .map(s -> s + " = ?")
                .collect(Collectors.joining(", "))
                + " WHERE id = ?";
    }
}
