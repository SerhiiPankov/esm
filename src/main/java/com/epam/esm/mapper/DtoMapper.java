package com.epam.esm.mapper;

public interface DtoMapper<T, Q, S> {
    T mapToModel(Q dto);

    S mapToDto(T t);
}
