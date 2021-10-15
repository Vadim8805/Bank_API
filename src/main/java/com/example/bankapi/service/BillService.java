package com.example.bankapi.service;

import com.example.bankapi.exceptions.FormatNumberException;
import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.model.Bill;
import com.example.bankapi.model.User;
import com.example.bankapi.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BillService {
    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill getBillById(int id) {
        Bill bill = billRepository.getBillById(id);
        if (bill == null) {
            throw new ResourceNotFoundException("Такого счета не существует.");
        }
        return bill;
    }

    public Bill updateBalance(Bill bill){
        if (bill.getBalance().compareTo(new BigDecimal(0)) < 0) {
            throw new FormatNumberException("Недостаточно средств на счету.");
        }
        return billRepository.updateBalance(bill);
    }
}
