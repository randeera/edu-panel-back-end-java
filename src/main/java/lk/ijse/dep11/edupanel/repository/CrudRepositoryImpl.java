package lk.ijse.dep11.edupanel.repository;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.SuperEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class CrudRepositoryImpl<T extends SuperEntity, ID extends Serializable>
        implements CrudRepository<T, ID>{

    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void update(T entity) {
        em.merge(entity);
    }

    @Override
    public void deleteById(ID pk) {
        em.remove(em.find(T.class, pk));
    }

    @Override
    public boolean existsById(ID pk) {
        return findById(pk).isPresent();
    }

    @Override
    public Optional<T> findById(ID pk) {
        return Optional.ofNullable(em.find(T.class, pk));
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM T e", T.class).getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM T e", Long.class).getSingleResult();
    }
}
