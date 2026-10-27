package com.fintech.backend.application.service;



import com.fintech.backend.domain.model.TransactionStatus;
import com.fintech.backend.domain.model.TransactionType;
import com.fintech.backend.domain.model.Transaction;
import com.fintech.backend.domain.port.TransactionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        sampleTransaction = new Transaction(
                UUID.randomUUID(),
                "ACC-12345",
                new BigDecimal("1500.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                Transaction.TransactionStatus.COMPLETED,
                "TXN-REF-001"
        );
    }

    @Test
    void createTransaction_shouldSaveAndReturnTransaction() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(sampleTransaction);

        Transaction result = transactionService.createTransaction(
                "ACC-12345",
                new BigDecimal("1500.00"),
                Transaction.TransactionType.DEPOSIT,
                "TXN-REF-001"
        );

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getTransactionById_shouldReturnTransactionWhenExists() {
        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(sampleTransaction));

        Optional<Transaction> result = transactionService.getTransactionById(UUID.randomUUID());

        assertTrue(result.isPresent());
        verify(transactionRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void getTransactionsByAccount_shouldReturnTransactionList() {
        when(transactionRepository.findByAccountId("ACC-12345"))
                .thenReturn(List.of(sampleTransaction));

        List<Transaction> results = transactionService.getTransactionsByAccount("ACC-12345");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(transactionRepository, times(1)).findByAccountId("ACC-12345");
    }

    @Test
    void getTransactionsByAccountAndType_shouldFilterByType() {
        when(transactionRepository.findByAccountIdAndType("ACC-12345", Transaction.TransactionType.DEPOSIT))
                .thenReturn(List.of(sampleTransaction));

        List<Transaction> results = transactionService.getTransactionsByAccountAndType(
                "ACC-12345", Transaction.TransactionType.DEPOSIT);

        assertFalse(results.isEmpty());
        verify(transactionRepository, times(1))
                .findByAccountIdAndType("ACC-12345", Transaction.TransactionType.DEPOSIT);
    }

    @Test
    void updateTransactionStatus_shouldUpdateAndReturn() {
        Transaction pendingTransaction = new Transaction(
                UUID.randomUUID(),
                "ACC-12345",
                new BigDecimal("1500.00"),
                Transaction.TransactionType.DEPOSIT,
                LocalDateTime.now(),
                Transaction.TransactionStatus.PENDING,
                "TXN-REF-001"
        );

        when(transactionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(pendingTransaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(pendingTransaction.withStatus(Transaction.TransactionStatus.COMPLETED));

        Optional<Transaction> result = transactionService.updateTransactionStatus(
                pendingTransaction.id(),
                Transaction.TransactionStatus.COMPLETED);

        assertTrue(result.isPresent());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}