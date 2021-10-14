package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;

import java.util.List;

public interface CardRepository {
    List<Card> getCards(int id);
    Bill getBalance(int id);
    Card getCardById(int id);
    Card save(Card card);
    Bill getBillByCardId(int id);
    void topUpBalance(Card card);
}
