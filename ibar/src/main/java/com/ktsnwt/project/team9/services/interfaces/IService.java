package com.ktsnwt.project.team9.services.interfaces;

public interface IService<T,I> {
	
	public Iterable<T> getAll();
	public T getById(I id);
	public T create(T entity) throws Exception;
	public boolean delete(I id) throws Exception;
	public T update(I id, T entity) throws Exception;
}
