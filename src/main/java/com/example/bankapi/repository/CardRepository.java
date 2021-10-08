package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Card> queryForCards(int id) {
        return entityManager.createQuery("select card from Card card where card.user.id = ?1")
                .setParameter(1, id)
                .getResultList();
    }

    public Bill getBalance(Card card) {
        Query query = entityManager.createQuery("select balance from Bill balance where balance.id = ?1")
                .setParameter(1, card.getBill().getId());
        return (Bill) query.getSingleResult();
    }

    public Card getCardBiId(int id) {
        return entityManager.find(Card.class, id);
    }

    public Card save(Card card, int userId, int billId) {
        card.setBill(entityManager.getReference(Bill.class, billId));
        card.setUser(entityManager.getReference(User.class, billId));
        entityManager.persist(card);
        return card;
    }
}