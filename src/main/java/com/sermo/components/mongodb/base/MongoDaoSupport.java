package com.sermo.components.mongodb.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author sermo
 * @version 2016年7月5日 
 */
public abstract class MongoDaoSupport<T> implements MongoDao<T>{
	
	private @Resource MongoTemplate mongoTemplate;
	
	private EntityMsg<T> entityMsg;
	
	public final static Criteria EMPTY_CRITERIA = new Criteria();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MongoDaoSupport() {
		this.entityMsg = new EntityMsg((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@Override
	public void insert(T model) {
		mongoTemplate.insert(model);
	}

	@Override
	public void insert(Collection<T> list) {
		mongoTemplate.insertAll(list);
	}

	@Override
	public void save(T model) {
		mongoTemplate.save(model);
	}

	@Override
	public void delete(Serializable key) {
		mongoTemplate.remove(new Query(Criteria.where(entityMsg.getKeyName()).is(key)), entityMsg.getCls());
	}

	@Override
	public void update(T model) {
		try {
			Query query = new Query(Criteria.where(entityMsg.getKeyName()).is(entityMsg.getKeyField().get(model)));
			Update update = new Update();
			for (Entry<Field, String> entry : this.entityMsg.getMapping().entrySet()) {
				update.set(entry.getValue(), entry.getKey().get(model));
			}
			mongoTemplate.updateFirst(query, update, entityMsg.getCls());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Criteria criteria, Update update) {
		mongoTemplate.updateFirst(new Query(criteria), update, entityMsg.getCls());
	}

	@Override
	public T select(Serializable key) {
		return mongoTemplate.findById(key, entityMsg.getCls());
	}

	@Override
	public T findOne(Criteria criteria) {
		return mongoTemplate.findOne(new Query(criteria), entityMsg.getCls());
	}

	@Override
	public T findOne(Criteria criteria, Sort sort) {
		return mongoTemplate.findOne(new Query(criteria).with(sort), entityMsg.getCls());
	}

	@Override
	public List<T> list() {
		return mongoTemplate.findAll(entityMsg.getCls());
	}

	@Override
	public List<T> list(Criteria criteria) {
		return mongoTemplate.find(new Query(criteria), entityMsg.getCls());
	}

	@Override
	public List<T> list(Integer start, Integer limit) {
		return mongoTemplate.find(new Query().skip(start).limit(limit), entityMsg.getCls());
	}

	@Override
	public List<T> list(Sort sort) {
		return mongoTemplate.find(new Query().with(sort), entityMsg.getCls());
	}

	@Override
	public List<T> list(Sort sort, Integer start, Integer limit) {
		return mongoTemplate.find(new Query().with(sort).skip(start).limit(limit), entityMsg.getCls());
	}

	@Override
	public List<T> list(Criteria criteria, Integer start, Integer limit) {
		return mongoTemplate.find(new Query(criteria).skip(start).limit(limit), entityMsg.getCls());
	}

	@Override
	public List<T> list(Criteria criteria, Sort sort, Integer start, Integer limit) {
		return mongoTemplate.find(new Query(criteria).with(sort).skip(start).limit(limit), entityMsg.getCls());
	}

	@Override
	public long count() {
		return mongoTemplate.count(new Query(), entityMsg.getCls());
	}

	@Override
	public long count(Criteria criteria) {
		return mongoTemplate.count(new Query(criteria), entityMsg.getCls());
	}

	@Override
	public T findAndModify(Criteria criteria, Update update) {
		return mongoTemplate.findAndModify(new Query(criteria), update, entityMsg.getCls());
	}
	
}
