package com.example.bankapi.controller;

import com.example.bankapi.controller.wrapper.WrapperBill;
import com.example.bankapi.controller.wrapper.WrapperCard;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping(value="/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Object> findAllObjects() {

        List<Object> objects = new ArrayList<Object>();
        return objects;
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody List<Object> resp) {
        WrapperCard wrapperCard = (WrapperCard) resp.get(0);
        User user = (User) resp.get(1);
        WrapperBill bill = (WrapperBill) resp.get(2);
        Card card = cardService.getCardById(wrapperCard.getId());
        cardService.create(card, user.getId(), bill.getId());
    }

//    @RequestMapping(value="/Object/getList/", method=RequestMethod.GET)
//    public @ResponseBody List<Object> findAllObjects() {
//
//        List<Object> objects = new ArrayList<Object>();
//        return objects;
//    }

    @GetMapping("/{userId}")
    public List<Card> getCards(@PathVariable int userId) {
        return cardService.getCards(userId);
    }

    @PostMapping(value = "/balance",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Double getBalance(@RequestBody WrapperCard wrapperCard ) {
        Card card = cardService.getCardById(wrapperCard.getId());
        return cardService.getBalance(card).getBalance();
    }
}
