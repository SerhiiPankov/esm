package com.epam.esm.repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.epam.esm.annotation.DbColumn;
import com.epam.esm.annotation.DbId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractRepository<T> {
    private static final Collector<CharSequence, ?, String> COLLECTOR = Collectors.joining(", ", " (", ") ");
    private final Class<T> clazz;
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

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
        return setId(t, keyHolder.getKeyAs(getIdField(t).getType()));
    }

    public int update(T t) throws ReflectiveOperationException {
        Map<String, Object> entityData = getEntityData(t);
        String sql = generateUpdateSqlQuery(entityData);
        Object id = getId(t);
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

    private Field getIdField(T t) throws NoSuchFieldException {
        for (Field field: clazz.getDeclaredFields()) {
            if (Arrays.stream(field.getAnnotations()).anyMatch(a -> a.annotationType() == DbId.class)) {
                return field;
            }
        }
        throw new NoSuchFieldException("Can't get id field from " + t.getClass() + " class");
    }

    private Object getId(T t) throws NoSuchFieldException, IllegalAccessException {
        Field idField = getIdField(t);
        idField.setAccessible(true);
        Object id = idField.get(t);
        idField.setAccessible(false);
        return id;
    }

    private T setId(T t, Object id) throws IllegalAccessException, NoSuchFieldException {
        Field idField = getIdField(t);
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

    private String generateUpdateSqlQuery(Map<String, Object> entityData) {
        return  "UPDATE " + clazz.getSimpleName() + " SET "
                + entityData.keySet().stream().map(s -> s + " ?").collect(Collectors.joining(", "))
                + " WHERE id = ?";
    }

    private Map<String, Object> getEntityData(T t) throws IllegalAccessException {
        Map<String, Object> entityMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            if (Arrays.stream(field.getAnnotations()).anyMatch(a -> a.annotationType() == DbColumn.class)
                    && field.get(t) != null) {
                entityMap.put(field.getName(), field.get(t));
            }
            field.setAccessible(false);
        }
        return entityMap;
    }
}
