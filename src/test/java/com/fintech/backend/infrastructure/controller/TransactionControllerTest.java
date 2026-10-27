package com.fintech.backend.infrastructure.controller;



import com.fintech.backend.domain.model.TransactionStatus;
import com.fintech.backend.domain.model.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.backend.application.service.TransactionService;
import com.fintech.backend.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private Transaction sampleTransaction;
    private String transactionJson;

    @BeforeEach
    void setUp() throws Exception {
        sampleTransaction = new Transaction(
                UUID.randomUUID(),
                "ACC-12345",
                new BigDecimal("1500.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                Transaction.TransactionStatus.COMPLETED,
                "TXN-REF-001"
        );

        transactionJson = """
                {
                    "accountId": "ACC-12345",
                    "amount": 1500.00,
                    "type": "DEPOSIT",
                    "reference": "TXN-REF-001"
                }
                """;
    }

    @Test
    void createTransaction_shouldReturn201WhenSuccessful() throws Exception {
        when(transactionService.createTransaction(anyString(), any(BigDecimal.class), 
                any(Transaction.TransactionType.class), anyString()))
                .thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountId").value("ACC-12345"))
                .andExpect(jsonPath("$.amount").value(1500.00));
    }

    @Test
    void getTransactionById_shouldReturn200WhenFound() throws Exception {
        when(transactionService.getTransactionById(any(UUID.class)))
                .thenReturn(Optional.of(sampleTransaction));

        mockMvc.perform(get("/api/v1/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountId").value("ACC-12345"));
    }

    @Test
    void getTransactionById_shouldReturn404WhenNotFound() throws Exception {
        when(transactionService.getTransactionById(any(UUID.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTransactionsByAccount_shouldReturn200WithList() throws Exception {
        when(transactionService.getTransactionsByAccount("ACC-12345"))
                .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/api/v1/transactions")
                        .param("accountId", "ACC-12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountId").value("ACC-12345"));
    }

    @Test
    void getTransactionsByAccountAndType_shouldFilterCorrectly() throws Exception {
        when(transactionService.getTransactionsByAccountAndType("ACC-12345", Transaction.TransactionType.DEPOSIT))
                .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/api/v1/transactions")
                        .param("accountId", "ACC-12345")
                        .param("type", "DEPOSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateTransactionStatus_shouldReturn200WhenUpdated() throws Exception {
        Transaction updatedTransaction = sampleTransaction.withStatus(Transaction.TransactionStatus.COMPLETED);
        
        when(transactionService.updateTransactionStatus(any(UUID.class), 
                eq(Transaction.TransactionStatus.COMPLETED)))
                .thenReturn(Optional.of(updatedTransaction));

        mockMvc.perform(patch("/api/v1/transactions/{id}/status", sampleTransaction.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"COMPLETED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}