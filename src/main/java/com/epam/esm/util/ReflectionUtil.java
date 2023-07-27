package com.epam.esm.util;

import com.epam.esm.annotation.DbColumn;
import com.epam.esm.annotation.DbId;
import com.epam.esm.exception.RepositoryReflectionException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

@Component
public class ReflectionUtil<T> {

    public Field getIdField(T t, Class<T> clazz) {
        for (Field field: clazz.getDeclaredFields()) {
            if (Arrays.stream(field.getAnnotations()).anyMatch(
                    a -> a.annotationType() == DbId.class)) {
                return field;
            }
        }
        throw new RepositoryReflectionException("Can't get id field from "
                + t.getClass().getSimpleName() + " class", new NoSuchFieldException());
    }

    public Object getId(T t, Class<T> clazz) {
        Field idField = getIdField(t, clazz);
        idField.setAccessible(true);
        Object id;
        try {
            id = idField.get(t);
        } catch (IllegalAccessException e) {
            throw new RepositoryReflectionException("Can't get id from "
                    + t.getClass().getSimpleName() + " object", e);
        }
        idField.setAccessible(false);
        return id;
    }

    public T setId(T t, Class<T> clazz, Object id) {
        Field idField = getIdField(t, clazz);
        idField.setAccessible(true);
        try {
            idField.set(t, id);
        } catch (IllegalAccessException e) {
            throw new RepositoryReflectionException("Can't set id into "
                    + t.getClass().getSimpleName() + " object", e);
        }
        idField.setAccessible(false);
        return t;
    }

    public Map<String, Object> getEntityData(T t, Class<T> clazz) {
        Map<String, Object> entityMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                if (Arrays.stream(field.getAnnotations()).anyMatch(
                        a -> a.annotationType() == DbColumn.class)
                        && field.get(t) != null) {
                    entityMap.put(field.getName(), field.get(t));
                }
            } catch (IllegalAccessException e) {
                throw new RepositoryReflectionException("Can't get data from "
                        + t.getClass().getSimpleName() + " object", e);
            }
            field.setAccessible(false);
        }
        return entityMap;
    }
}
