package com.example.bankapi.repository;

import com.example.bankapi.model.Bill;
import com.example.bankapi.model.User;

public interface BillRepository {
    User getUserByBillId(int id);
    Bill getBillById(int id);
    Bill updateBalance(Bill bill);
}
