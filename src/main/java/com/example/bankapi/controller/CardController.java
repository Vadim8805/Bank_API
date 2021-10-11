package com.example.bankapi.controller;

import com.example.bankapi.Util.CardUtil;
import com.example.bankapi.controller.wrapper.WrapperCard;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.service.BillService;
import com.example.bankapi.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {
    private final CardService cardService;
    private final BillService billService;

    @Autowired
    public CardController(CardService cardService, BillService billService) {
        this.cardService = cardService;
        this.billService = billService;
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody List<Object> req) {
        int billId = CardUtil.getBillId(req);
        Bill bill = billService.getBillById(billId);
        User user = billService.getUserByBillId(billId);
        String cardNumber = CardUtil.getCardNumber(req);
        Card card = new Card(user, bill, cardNumber);
        cardService.create(card);
    }

    @GetMapping("/{userId}")
    public List<Card> getCards(@PathVariable int userId) {
        return cardService.getCards(userId);
    }

    @PostMapping(value = "/balance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Double getBalance(@RequestBody WrapperCard wrapperCard ) {
        Card card = cardService.getCardById(wrapperCard.getId());
        return cardService.getBalance(card).getBalance();
    }

    @PostMapping(value = "/topUpBalance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void topUpBalance(@RequestBody List<Object> req) {
        int cardId = CardUtil.getCardId(req);
        Bill bill = cardService.getBillByCardId(cardId);
        bill.setBalance(bill.getBalance() + CardUtil.deposit(req));
        billService.topUpBalance(bill);
    }
}
