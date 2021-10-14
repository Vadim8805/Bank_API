package com.example.bankapi.controller;

import com.example.bankapi.Util.CardUtil;
import com.example.bankapi.Util.ContragentUtil;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Contragent;
import com.example.bankapi.model.User;
import com.example.bankapi.service.BillService;
import com.example.bankapi.service.ContragentService;
import com.example.bankapi.service.UserService;
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
@RequestMapping(value = "/rest/contragents", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContragentController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ContragentService contragentService;
    private final UserService userService;
    private final BillService billService;

    @Autowired
    public ContragentController(ContragentService contragentService, UserService userService, BillService billService) {
        this.contragentService = contragentService;
        this.userService = userService;
        this.billService = billService;
    }

    @GetMapping("/{userId}")
    public List<Contragent> getContragents(@PathVariable int userId) {
        log.info("get contragents for user {}", userId);
        return contragentService.getContragents(userId);
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contragent> createCard(@RequestBody List<Object> req) {
        Contragent contragent = contragentFromRequest(req);
        log.info("create contragent {} for user {}", contragent.getId(), contragent.getUser().getId());
        return new ResponseEntity<>(contragentService.create(contragent), HttpStatus.CREATED);
    }

    @PostMapping(value = "/moneyTransfer",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bill> moneyTransfer(@RequestBody List<Object> req) {
        int billId = ContragentUtil.getBillIdMoneyTransferReq(req);
        int contragentId = ContragentUtil.getContragentIdMoneyTransferReq(req);
        Bill bill = billService.getBillById(billId);
        bill.setBalance(bill.getBalance().subtract(ContragentUtil.getMoneyTransfer(req)));
        Contragent contragent = contragentService.getContragentById(contragentId);
        contragent.setDepositSum(contragent.getDepositSum().add(ContragentUtil.getMoneyTransfer(req)));
        ResponseEntity<Bill> billResponseEntity = new ResponseEntity<>(billService.updateBalance(bill), HttpStatus.CREATED);
        contragentService.update(contragent);
        log.info("money transfered {} for bill {}", ContragentUtil.getMoneyTransfer(req), bill.getId());
        return billResponseEntity;
    }

    private Contragent contragentFromRequest(List<Object> req){
        int userId = ContragentUtil.getContragentUserIdCreateCardReq(req);
        String billNumber = ContragentUtil.getContragentBillNumberCreateCardReq(req);
        User user = userService.getUserById(userId);
        return new Contragent(user, billNumber, new BigDecimal(0));
    }
}
