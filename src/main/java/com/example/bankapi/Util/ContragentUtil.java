package com.example.bankapi.Util;

import com.example.bankapi.exceptions.FormatNumberException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContragentUtil {
    public static int getContragentUserIdCreateCardReq(List<Object> list) {
        Map<String, Integer> cardMap = (HashMap<String, Integer>) list.get(0);
        return cardMap.get("id");
    }

    public static String getContragentBillNumberCreateCardReq(List<Object> list){
        Map<String, String> contragentMap = (HashMap<String, String>) list.get(1);
        return contragentMap.get("billNumber");
    }

    public static int getBillIdMoneyTransferReq(List<Object> list){
        Map<String, Integer> contragentMap = (HashMap<String, Integer>) list.get(0);
        return contragentMap.get("id");
    }

    public static int getContragentIdMoneyTransferReq(List<Object> list){
        Map<String, Integer> contragentMap = (HashMap<String, Integer>) list.get(1);
        return contragentMap.get("id");
    }

    public static BigDecimal getMoneyTransfer(List<Object> list){
        Map<String, BigDecimal> moneyTransfer = (HashMap<String, BigDecimal>) list.get(2);
        try {
            return new BigDecimal(String.valueOf(moneyTransfer.get("moneyTransfer")));
        } catch (NumberFormatException e) {
            throw new FormatNumberException("Значение должно быть числовым.");
        }
    }

    public static void badNumber(String billNumber) {
        try {
            new BigDecimal(billNumber);
        } catch (NumberFormatException e) {
            throw new FormatNumberException("Номер счета должен содержать 20 цифр.");
        }
        if (billNumber.length() != 20) {
            throw new FormatNumberException("Номер счета должен содержать 20 цифр.");
        }
    }
}