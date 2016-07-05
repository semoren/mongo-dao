package com.sermo.components.mongodb.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author sermo
 * @version 2016年7月5日 
 */
public interface MongoDao<T> {
	
	void insert(T model);
	
	void insert(Collection<T> list);
	
	void save(T model);
	
	void delete(Serializable key);
	
	void update(T model);
	
	void update(Criteria criteria, Update update);
	
	T select(Serializable key);
	
	T findOne(Criteria criteria);
	
	T findOne(Criteria criteria, Sort sort);
	
	List<T> list();
	
	List<T> list(Criteria criteria);
	
	List<T> list(Integer start, Integer limit);
	
	List<T> list(Sort sort);
	
	List<T> list(Sort sort, Integer start, Integer limit);
	
	List<T> list(Criteria criteria, Integer start, Integer limit);
	
	List<T> list(Criteria criteria, Sort sort, Integer start, Integer limit);
	
	long count();

	long count(Criteria criteria);

	T findAndModify(Criteria criteria, Update update);
	
}
