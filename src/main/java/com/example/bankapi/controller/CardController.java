package com.example.bankapi.controller;

import com.example.bankapi.Util.CardUtil;
import com.example.bankapi.controller.wrapper.WrapperCard;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.service.BillService;
import com.example.bankapi.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CardService cardService;
    private final BillService billService;

    @Autowired
    public CardController(CardService cardService, BillService billService) {
        this.cardService = cardService;
        this.billService = billService;
    }

    @GetMapping("/{userId}")
    public List<Card> getCards(@PathVariable int userId) {
        log.info("get cards for user {}", userId);
        return cardService.getCards(userId);
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> createCard(@RequestBody List<Object> req) {
        Card card = cardFromRequest(req);
        log.info("create card {} for user {}", card.getId(), card.getUser().getId());
        return new ResponseEntity<>(cardService.create(card), HttpStatus.CREATED);
    }

    @PostMapping(value = "/balance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal getBalance(@RequestBody WrapperCard wrapperCard ) {
        Card card = cardService.getCardById(wrapperCard.getId());
        log.info("get balance for card {} for user {}", card.getId(), card.getUser().getId());
        return cardService.getBalance(card).getBalance();
    }

    @PostMapping(value = "/topUpBalance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bill> topUpBalance(@RequestBody List<Object> req) {
        int cardId = CardUtil.getCardId(req);
        Bill bill = cardService.getCardById(cardId).getBill();
        bill.setBalance(bill.getBalance().add(CardUtil.deposit(req)));
        log.info("top up balance for {} for bill {}", CardUtil.deposit(req), bill.getId());
        return new ResponseEntity<>(billService.updateBalance(bill), HttpStatus.CREATED);
    }

    private Card cardFromRequest(List<Object> req){
        int billId = CardUtil.getBillId(req);
        Bill bill = billService.getBillById(billId);
        User user = billService.getBillById(billId).getUser();
        String cardNumber = CardUtil.getCardNumber(req);
        return new Card(user, bill, cardNumber);
    }
}
