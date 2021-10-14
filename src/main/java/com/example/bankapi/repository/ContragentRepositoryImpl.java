package com.example.bankapi.repository;

import com.example.bankapi.model.Contragent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ContragentRepositoryImpl implements ContragentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Contragent> getContragents(int id) {
        return entityManager.createQuery("Select c FROM Contragent c where c.user.id= ?1", Contragent.class)
                .setParameter(1, id)
                .getResultList();
    }

    public Contragent getContragentById(int id) {
        return entityManager.find(Contragent.class, id);
    }

    @Transactional
    public Contragent save(Contragent contragent) {
        entityManager.persist(contragent);
        entityManager.flush();
        return contragent;
    }

    @Transactional
    public Contragent update(Contragent contragent) {
        entityManager.merge(contragent);
        entityManager.flush();
        return contragent;
    }
}
