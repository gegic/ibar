package com.ktsnwt.project.team9.helper.interfaces;

public interface IMapper<T,U> {

    T toEntity(U dto);

    U toDto(T entity);
}
