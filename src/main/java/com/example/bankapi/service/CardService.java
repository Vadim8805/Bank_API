package com.example.bankapi.service;

import com.example.bankapi.controller.wrapper.WrapperBill;
import com.example.bankapi.controller.wrapper.WrapperCard;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;

    public List<Card> getCards(int id) {
        return cardRepository.queryForCards(id);
    }

    public Bill getBalance(Card card) {
        return cardRepository.getBalance(card);
    }

    public Card getCardById(int id) {
        return cardRepository.getCardBiId(id);
    }

    public Card create(Card card, int billId, int userId){
        return cardRepository.save(card, billId, userId);
    }
}
