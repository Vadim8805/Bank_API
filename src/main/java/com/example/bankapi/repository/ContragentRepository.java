package com.example.bankapi.repository;

import com.example.bankapi.model.Contragent;

import java.util.List;

public interface ContragentRepository {
    List<Contragent> getContragents(int id);
    Contragent getContragentById(int id);
    Contragent save(Contragent contragent);
    Contragent update(Contragent contragent);
}
