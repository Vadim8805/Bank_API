package com.example.bankapi;

import com.example.bankapi.exceptions.FormatNumberException;
import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.repository.BillRepository;
import com.example.bankapi.repository.ContragentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/db/initDB.sql", "/db/populateDB.sql"})
public class ContragentControllerTest {
    private final MockMvc mockMvc;
    private final BillRepository billRepository;
    private final ContragentRepository contragentRepository;

    @Autowired
    public ContragentControllerTest(MockMvc mockMvc, BillRepository billRepository, ContragentRepository contragentRepository) {
        this.mockMvc = mockMvc;
        this.billRepository = billRepository;
        this.contragentRepository = contragentRepository;
    }

    @Test
    public void testGetContragents() throws Exception {
        String cardsExpected = getContragentsJsonExpected();
        String url = "/rest/contragents/10";
        mockMvc.perform(
                        get(url)
                ).andExpect(status().isOk())
                .andExpect(content().json(cardsExpected))
                .andDo(print());
    }

    @Test
    public void testGetContragentsBadUserId() throws Exception {
        String url = "/rest/contragents/110";
        mockMvc.perform(
                        get(url)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Держателя карты с таким id не существует.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testCreateContragent() throws Exception {
        String jsonInput = getInputContragentsJson();
        String url = "/rest/contragents/create";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testCreateCardBadUserId() throws Exception {
        String jsonInput = getInputJsonForContragentCreateBadUserId();
        String url = "/rest/contragents/create";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Держателя карты с таким id не существует.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testCreateContragentBadFormatNumber() throws Exception {
        String jsonInput = getInputJsonForContragentsBadFormatNumber();
        String url = "/rest/contragents/create";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FormatNumberException))
                .andExpect(result -> assertEquals("Номер счета должен содержать 20 цифр.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testTopUpBalance() throws Exception {
        String jsonInput = getInputMoneyTransferJson();
        String url = "/rest/contragents/moneyTransfer";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isCreated())
                .andDo(print());
        assertThat(billRepository.getBillById(11).getBalance()).isEqualTo(new BigDecimal("898.54"));
        assertThat(contragentRepository.getContragentById(10).getDepositSum()).isEqualTo(new BigDecimal("306.55"));
    }

    @Test
    public void testTopUpBalanceBadBillId() throws Exception {
        String jsonInput = getInputMoneyTransferJsonBadBillId();
        String url = "/rest/contragents/moneyTransfer";
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
    public void testTopUpBalanceBadContragentId() throws Exception {
        String jsonInput = getInputMoneyTransferJsonBadContragentId();
        String url = "/rest/contragents/moneyTransfer";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Добавьте контрагента.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    public void testTopUpBalanceBadFormatNumber() throws Exception {
        String jsonInput = getInputMoneyTransferJsonBadFormatNumber();
        String url = "/rest/contragents/moneyTransfer";
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

    @Test
    public void testTopUpBalanceNotEnoughMoney() throws Exception {
        String jsonInput = getInputMoneyTransferJsonNotEnoughMoney();
        String url = "/rest/contragents/moneyTransfer";
        mockMvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FormatNumberException))
                .andExpect(result -> assertEquals("Недостаточно средств на счету.",
                        result.getResolvedException().getMessage()))
                .andDo(print());
    }

    private static String getContragentsJsonExpected() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 10);
        card1Expected.put("billNumber", "89865487569463809864");
        card1Expected.put("depositSum", 205);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 11);
        card2Expected.put("billNumber", "18274984678546720476");
        card2Expected.put("depositSum", 0);
        req.add(card1Expected);
        req.add(card2Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputContragentsJson() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 10);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("billNumber", "99999000003333388888");
        req.add(card1Expected);
        req.add(card2Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForContragentCreateBadUserId() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 1001);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("billNumber", "99999000003333388888");
        req.add(card1Expected);
        req.add(card2Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputJsonForContragentsBadFormatNumber() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 10);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("billNumber", "99999000003333388888ывау");
        req.add(card1Expected);
        req.add(card2Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputMoneyTransferJson() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 11);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 10);
        Map<String, Object> card3Expected = new HashMap<>();
        card3Expected.put("moneyTransfer", 101.55);
        req.add(card1Expected);
        req.add(card2Expected);
        req.add(card3Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputMoneyTransferJsonBadBillId() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 1568);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 10);
        Map<String, Object> card3Expected = new HashMap<>();
        card3Expected.put("moneyTransfer", 101.55);
        req.add(card1Expected);
        req.add(card2Expected);
        req.add(card3Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputMoneyTransferJsonBadContragentId() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 11);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 1093);
        Map<String, Object> card3Expected = new HashMap<>();
        card3Expected.put("moneyTransfer", 101.55);
        req.add(card1Expected);
        req.add(card2Expected);
        req.add(card3Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputMoneyTransferJsonBadFormatNumber() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 11);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 10);
        Map<String, Object> card3Expected = new HashMap<>();
        card3Expected.put("moneyTransfer", "sdf");
        req.add(card1Expected);
        req.add(card2Expected);
        req.add(card3Expected);
        return om.writeValueAsString(req);
    }

    private static String getInputMoneyTransferJsonNotEnoughMoney() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<Object> req = new ArrayList<>();
        Map<String, Object> card1Expected = new HashMap<>();
        card1Expected.put("id", 11);
        Map<String, Object> card2Expected = new HashMap<>();
        card2Expected.put("id", 10);
        Map<String, Object> card3Expected = new HashMap<>();
        card3Expected.put("moneyTransfer", 100000);
        req.add(card1Expected);
        req.add(card2Expected);
        req.add(card3Expected);
        return om.writeValueAsString(req);
    }
}
