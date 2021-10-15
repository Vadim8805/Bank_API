package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class BillRepositoryImpl implements BillRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Bill getBillById(int id) {
        return entityManager.find(Bill.class, id);
    }

    @Transactional
    public Bill updateBalance(Bill bill) {
        entityManager.merge(bill);
        entityManager.flush();
        return bill;
    }
}
