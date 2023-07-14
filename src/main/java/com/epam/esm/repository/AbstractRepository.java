package com.epam.esm.repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractRepository<T> {
    private static final String ID_COLUMN = "id";
    private static final Collector COLLECTOR = Collectors.joining(", ", " (", ") ");
    protected final JdbcTemplate jdbcTemplate;
    private final Class<T> clazz;
    private final RowMapper<T> rowMapper;

    public AbstractRepository(JdbcTemplate jdbcTemplate, Class<T> clazz, RowMapper<T> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.clazz = clazz;
        this.rowMapper = rowMapper;
    }

    public T create(T t) throws ReflectiveOperationException {
        Map<String, Object> entityData = getEntityData(t);
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
        return setId(t, keyHolder.getKey().longValue());
    }

    public int update(T t) throws ReflectiveOperationException {
        String sql = "UPDATE " + clazz.getSimpleName() + " SET " + getParameterForUpdateQuery(t)
                + " WHERE id = " + getId(t);
        return jdbcTemplate.update(sql);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM " + clazz.getSimpleName()
                + " WHERE id = " + id;
        jdbcTemplate.execute(sql);
    }

    public Optional<T> get(Long id) {
        String sql = "SELECT * FROM " + clazz.getSimpleName()
                + " WHERE id = " + id;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper));
    }

    public List<T> getAll() {
        String sql = "SELECT * FROM " + clazz.getSimpleName();
        List<T> ts = jdbcTemplate.query(sql, rowMapper);
        return ts;
    }

    private String getParameterForUpdateQuery(T t) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        StringJoiner columnValues = new StringJoiner(", ");
        for (Field field: fields) {
            field.setAccessible(true);
            if (field.get(t) != null) {
                columnValues.add(field.getName() + " = '" + field.get(t).toString() + "' ");
            }
            field.setAccessible(false);
        }
        return columnValues.toString();
    }

    private long getId(T t) throws IllegalAccessException, NoSuchFieldException {
        Field idField = clazz.getDeclaredField(ID_COLUMN);
        idField.setAccessible(true);
        Long id = (Long) idField.get(t);
        idField.setAccessible(false);
        return id;
    }

    private T setId(T t, Long id) throws IllegalAccessException, NoSuchFieldException {
        Field idField = clazz.getDeclaredField(ID_COLUMN);
        idField.setAccessible(true);
        idField.set(t, id);
        idField.setAccessible(false);
        return t;
    }

    private String generateInsertSqlQuery(Map<String, Object> entityData) {
        return "INSERT INTO " + clazz.getSimpleName()
                + entityData.keySet().stream().collect(COLLECTOR)
                + "VALUES "
                + entityData.keySet().stream()
                        .map(value -> "?").collect(COLLECTOR);
    }

    private Map<String, Object> getEntityData(T t) throws IllegalAccessException {
        Map<String, Object> entityMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            if (field.get(t) != null) {
                entityMap.put(field.getName(), field.get(t));
            }
            field.setAccessible(false);
        }
        return entityMap;
    }
}
