package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
@Transactional
public class BillRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public User getUserByBillId(int id) {
        Query query = entityManager.createQuery("select u from User u Inner Join Bill b on u.id = b.user.id where b.id =?1")
                .setParameter(1, id);
        return (User) query.getSingleResult();
    }

    public Bill getBillById(int id) {
        return entityManager.find(Bill.class, id);
    }

    public Bill topUpBalance(Bill bill) {
        entityManager.merge(bill);
        entityManager.flush();
        return bill;
    }
}
