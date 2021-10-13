package com.example.bankapi.Util;

import com.example.bankapi.exceptions.FormatNumberException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardUtil {
    public static String getCardNumber(List<Object> list) {
        Map<String, String> cardMap = (HashMap<String, String>) list.get(0);
        return cardMap.get("number");
    }

    public static int getBillId(List<Object> list){
        Map<String, Integer> billMap = (HashMap<String, Integer>) list.get(1);
        return billMap.get("id");
    }

    public static int getCardId(List<Object> list){
        Map<String, Integer> cardMap = (HashMap<String, Integer>) list.get(0);
        return  cardMap.get("id");
    }

    public static BigDecimal deposit(List<Object> list) {
        Map<String, String> cardMap = (HashMap<String, String>) list.get(1);
        try {
            return new BigDecimal(cardMap.get("moneyDeposited"));
        } catch (NumberFormatException e) {
            throw new FormatNumberException("Значение должно быть числовым.");
        }
    }

    public static void badNumber(String cardNumber) {
        try {
            new BigDecimal(cardNumber);
        } catch (NumberFormatException e) {
            throw new FormatNumberException("Номер карты должен содержать 16 цифр.");
        }
        if (cardNumber.length() != 16) {
            throw new FormatNumberException("Номер карты должен содержать 16 цифр.");
        }
    }
}
