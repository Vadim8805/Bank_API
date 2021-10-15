package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;

public interface BillRepository {
    Bill getBillById(int id);
    Bill updateBalance(Bill bill);
}
