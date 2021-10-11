package com.example.bankapi.service;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.Card;
import com.example.bankapi.model.User;
import com.example.bankapi.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {
    BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public User getUserByBillId(int id) {
        return billRepository.getUserByBillId(id);
    }

    public Bill getBillById(int id) {
        return billRepository.getBillById(id);
    }

    public void topUpBalance(Bill bill){
        billRepository.topUpBalance(bill);
    }
}
