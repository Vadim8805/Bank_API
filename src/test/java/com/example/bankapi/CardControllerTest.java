package com.example.bankapi;

import com.example.bankapi.exceptions.FormatNumberException;
import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.repository.BillRepository;
import com.example.bankapi.repository.BillRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/db/initDB.sql", "/db/populateDB.sql"})
public class CardControllerTest {
    private final MockMvc mockMvc;
    private final BillRepository billRepository;

    @Autowired
    public CardControllerTest(MockMvc mockMvc, BillRepository billRepository) {
        this.mockMvc = mockMvc;
        this.billRepository = billRepository;
    }

    @Test
    public void testGetCards() throws Exception {
        String cardsExpected = getCardsJsonExpected();
        String url = "/rest/cards/11";
        mockMvc.perform(
                get(url)
                ).andExpect(status().isOk())
                .andExpect(content().json(cardsExpected))
                .andDo(print());
    }

    @Test
    public void testGetCardsBadUserId() throws Exception {
        String url = "/rest/cards/110";
        mockMvc.perform(
                        get(url)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Держателя карты с таким id не существует.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testCreateCard() throws Exception {
        String jsonInput = getInputJsonForCardCreate();
        String url = "/rest/cards/create";
        mockMvc.perform(
                    post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput)
                ).andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testCreateCardBadBillId() throws Exception {
        String jsonInput = getInputJsonForCardCreateBadBillId();
        String url = "/rest/cards/create";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Такого счета не существует.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testCreateCardBadFormatNumber() throws Exception {
        String jsonInput = getInputJsonForCardCreateBadFormatNumber();
        String url = "/rest/cards/create";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FormatNumberException))
                .andExpect(result -> assertEquals("Номер карты должен содержать 16 цифр.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testGetBalance() throws Exception {
        String jsonInput = getInputJsonForGetBalance();
        String url = "/rest/cards/balance";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isOk())
                .andExpect(content().string("1000.09"))
                .andDo(print());
    }

    @Test
    public void testGetBalanceBadCardId() throws Exception {
        String jsonInput = getInputJsonForGetBalanceBadCard();
        String url = "/rest/cards/balance";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Карта не найдена.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testTopUpBalance() throws Exception {
        String jsonInput = getInputJsonForTopUpBalance();
        String url = "/rest/cards/topUpBalance";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isCreated())
                .andDo(print());
        assertThat(billRepository.getBillById(11).getBalance()).isEqualTo(new BigDecimal("1555.09"));
    }

    @Test
    public void testTopUpBalanceBadCardId() throws Exception {
        String jsonInput = getInputJsonForTopUpBalanceBadCardId();
        String url = "/rest/cards/topUpBalance";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Счет не найден, карта не найдена.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testTopUpBalanceBadDepositFormatNumber() throws Exception {
        String jsonInput = getInputJsonForTopUpBalanceBadDepositFormatNumber();
        String url = "/rest/cards/topUpBalance";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FormatNumberException))
                .andExpect(result -> assertEquals("Значение должно быть числовым.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    private static String getCardsJsonExpected() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 11);
        card1Expected.put("number", "2345678901234567");
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 12);
        card2Expected.put("number", "3456789012345678");
        req.add(card1Expected);
        req.add(card2Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForCardCreate() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, String> cardMap = new HashMap<>();
        cardMap.put("number", "8424567857123847");
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("id", 10);
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForCardCreateBadBillId() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, String> cardMap = new HashMap<>();
        cardMap.put("number", "8424567857123847");
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("id", 5235);
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForCardCreateBadFormatNumber() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, String> cardMap = new HashMap<>();
        cardMap.put("number", "842456785712384700");
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("id", 10);
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForGetBalance() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Integer> cardMap = new HashMap<>();
        cardMap.put("id", 12);
        return om.writeValueAsString(cardMap);
    }

    private static String getInputJsonForGetBalanceBadCard() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Integer> cardMap = new HashMap<>();
        cardMap.put("id", 1265);
        return om.writeValueAsString(cardMap);
    }

    private static String getInputJsonForTopUpBalance() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("id", 11);
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("moneyDeposited", "555.00");
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForTopUpBalanceBadCardId() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("id", 8545);
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("moneyDeposited", "555.00");
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForTopUpBalanceBadDepositFormatNumber() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("id", 10);
        Map<String, Object> billMap = new HashMap<>();
        billMap.put("moneyDeposited", "кенпарп");
        req.add(cardMap);
        req.add(billMap);
        return om.writeValueAsString(req);
    }
}