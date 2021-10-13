package com.example.bankapi.service;

import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.model.Bill;
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
        Bill bill = billRepository.getBillById(id);
        if (bill == null) {
            throw new ResourceNotFoundException("Такого счета не существует.");
        }
        return bill;
    }

    public Bill topUpBalance(Bill bill){
        return billRepository.topUpBalance(bill);
    }
}
