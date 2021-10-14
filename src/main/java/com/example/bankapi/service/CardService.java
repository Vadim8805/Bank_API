package com.example.bankapi.service;

import com.example.bankapi.Util.CardUtil;
import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards(int id) {
        List<Card> cards = cardRepository.getCards(id);
        if (cards.size() == 0) {
            throw new ResourceNotFoundException("Держателя карты с таким id не существует.");
        }
        return cards;
    }

    public Bill getBalance(Card card) {
        return cardRepository.getBalance(card.getBill().getId());
    }

    public Card getCardById(int id) {
        Card card = cardRepository.getCardById(id);
        if (card == null) {
            throw new ResourceNotFoundException("Карта не найдена.");
        }
        return card;
    }

    public Card create(Card card){
        CardUtil.badNumber(card.getNumber());
        return cardRepository.save(card);
    }

    public Bill getBillByCardId(int id) {
        Bill bill = cardRepository.getBillByCardId(id);
        System.out.println();
        if (bill == null) {
            throw new ResourceNotFoundException("Счет не найден, карта не найдена.");
        }
        return cardRepository.getBillByCardId(id);
    }
}
