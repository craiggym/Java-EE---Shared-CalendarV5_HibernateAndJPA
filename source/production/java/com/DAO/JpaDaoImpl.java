package com.DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class JpaDaoImpl<T, ID extends Serializable> implements JpaDao<T, ID> {
    private Class<T> persistentClass;

    private EntityManager entityManager;

    public JpaDaoImpl(Class<T> persistentClass){
        this.persistentClass = persistentClass;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T save(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        T mergedEntity = getEntityManager().merge(entity);
        return mergedEntity;
    }

    @Override
    public void delete(T entity) {
        entity = getEntityManager().merge(entity);
        getEntityManager().remove(entity);
    }


    @Transactional
    @Override
    public T findById(ID id) {
        T entity = getEntityManager().find(getPersistentClass(), id);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return getEntityManager()
                .createQuery("select x from " + getPersistentClass().getSimpleName() + " x")
                .getResultList();
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }
}
