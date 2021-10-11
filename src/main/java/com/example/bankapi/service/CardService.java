package com.example.bankapi.service;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards(int id) {
        return cardRepository.queryForCards(id);
    }

    public Bill getBalance(Card card) {
        return cardRepository.getBalance(card);
    }

    public Card getCardById(int id) {
        Card card = cardRepository.getCardById(id);
        return card;
    }

    public void create(Card card){
        cardRepository.save(card);
    }

    public void topUpBalance(Card card){
        cardRepository.topUpBalance(card);
    }

    public Bill getBillByCardId(int id) {
        return cardRepository.getBillByCardId(id);
    }
}
