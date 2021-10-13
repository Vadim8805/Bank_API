package com.example.bankapi.controller;

import com.example.bankapi.Util.CardUtil;
import com.example.bankapi.controller.wrapper.WrapperCard;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.service.BillService;
import com.example.bankapi.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/{userId}")
    public List<Card> getCards(@PathVariable int userId) {
        return cardService.getCards(userId);
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> createCard(@RequestBody List<Object> req) {
        Card card = cardFromRequest(req);
        return new ResponseEntity<>(cardService.create(card), HttpStatus.CREATED);
    }

    @PostMapping(value = "/balance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal getBalance(@RequestBody WrapperCard wrapperCard ) {
        Card card = cardService.getCardById(wrapperCard.getId());
        return cardService.getBalance(card).getBalance();
    }

    @PostMapping(value = "/topUpBalance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bill> topUpBalance(@RequestBody List<Object> req) {int cardId = CardUtil.getCardId(req);
        Bill bill = cardService.getBillByCardId(cardId);
        bill.setBalance(bill.getBalance().add(CardUtil.deposit(req)));
        return new ResponseEntity<>(billService.topUpBalance(bill), HttpStatus.CREATED);
    }

    private Card cardFromRequest(List<Object> req){
        int billId = CardUtil.getBillId(req);
        Bill bill = billService.getBillById(billId);
        User user = billService.getUserByBillId(billId);
        String cardNumber = CardUtil.getCardNumber(req);
        return new Card(user, bill, cardNumber);
    }
}
