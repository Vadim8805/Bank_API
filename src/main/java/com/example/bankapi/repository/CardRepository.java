package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Card> queryForCards(int id) {
        return entityManager.createQuery("Select c FROM Card c where c.user.id= ?1", Card.class)
                .setParameter(1, id)
                .getResultList();
    }

    public Bill getBalance(Card card) {
        Query query = entityManager.createQuery("select b from Bill b where b.id = ?1")
                .setParameter(1, card.getBill().getId());
        return (Bill) query.getSingleResult();
    }

    @Transactional
    public Card getCardById(int id) {
        return entityManager.find(Card.class, id);
    }

    public Card save(Card card) {
        entityManager.persist(card);
        entityManager.flush();
        return card;
    }

    public Bill getBillByCardId(int id) {
        Query query = entityManager.createQuery("select b from Bill b Inner Join Card c on b.id = c.bill.id where b.id =?1")
                .setParameter(1, id);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return (Bill) query.getSingleResult();
    }

    public Card getCardByNumber(String number) {
        Query query = entityManager.createQuery("select c from Card c where c.number = ?1")
                .setParameter(1, number);
        return (Card) query.getSingleResult();
    }

    public void topUpBalance(Card card) {
        entityManager.merge(card);
    }
}