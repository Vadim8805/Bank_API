package com.example.bankapi.service;

import com.example.bankapi.Util.ContragentUtil;
import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.model.Contragent;
import com.example.bankapi.repository.ContragentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContragentService {
    private final ContragentRepository contragentRepository;

    @Autowired
    public ContragentService(ContragentRepository contragentRepository) {
        this.contragentRepository = contragentRepository;
    }

    public List<Contragent> getContragents(int id) {
        List<Contragent> contragents = contragentRepository.getContragents(id);
        if (contragents.size() == 0) {
            throw new ResourceNotFoundException("Держателя карты с таким id не существует.");
        }
        return contragents;
    }

    public Contragent getContragentById(int id) {
        Contragent contragent = contragentRepository.getContragentById(id);
        if (contragent == null) {
            throw new ResourceNotFoundException("Добавьте контрагента.");
        }
        return contragent;
    }

    public Contragent create(Contragent contragent){
        ContragentUtil.badNumber(contragent.getBillNumber());
        return contragentRepository.save(contragent);
    }

    public Contragent update(Contragent contragent){
        ContragentUtil.badNumber(contragent.getBillNumber());
        return contragentRepository.update(contragent);
    }
}