package com.fintech.backend.infrastructure.controller;

import com.fintech.backend.application.service.TransactionService;
import com.fintech.backend.domain.model.Transaction;
import com.fintech.backend.domain.model.Transaction.TransactionStatus;
import com.fintech.backend.domain.model.Transaction.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public record CreateTransactionRequest(
        String accountId,
        BigDecimal amount,
        TransactionType type,
        String reference
    ) {}

    public record UpdateTransactionRequest(
        TransactionStatus status
    ) {}

    public record TransactionResponse(
        UUID id,
        String accountId,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime timestamp,
        TransactionStatus status,
        String reference
    ) {
        public static TransactionResponse fromDomain(Transaction transaction) {
            return new TransactionResponse(
                transaction.id(),
                transaction.accountId(),
                transaction.amount(),
                transaction.type(),
                transaction.timestamp(),
                transaction.status(),
                transaction.reference()
            );
        }
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        logger.info("Creating transaction for account: {}, type: {}, amount: {}", 
            request.accountId(), request.type(), request.amount());
        
        if (request.accountId() == null || request.accountId().isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (request.type() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }

        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            request.accountId(),
            request.amount(),
            request.type(),
            LocalDateTime.now(),
            TransactionStatus.PENDING,
            request.reference()
        );

        Transaction created = transactionService.createTransaction(transaction);
        logger.info("Transaction created successfully with ID: {}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.fromDomain(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
        logger.debug("Fetching transaction by ID: {}", id);
        
        return transactionService.getTransactionById(id)
            .map(transaction -> {
                logger.info("Transaction found: {}", id);
                return ResponseEntity.ok(TransactionResponse.fromDomain(transaction));
            })
            .orElseGet(() -> {
                logger.warn("Transaction not found: {}", id);
                return ResponseEntity.notFound().build();
            });
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        logger.info("Fetching all transactions");
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionResponse> responses = transactions.stream()
            .map(TransactionResponse::fromDomain)
            .toList();
        logger.info("Found {} transactions", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable String accountId) {
        logger.info("Fetching transactions for account: {}", accountId);
        
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        List<TransactionResponse> responses = transactions.stream()
            .map(TransactionResponse::fromDomain)
            .toList();
        logger.info("Found {} transactions for account: {}", responses.size(), accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}/type/{type}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountAndType(
            @PathVariable String accountId,
            @PathVariable TransactionType type) {
        logger.info("Fetching transactions for account: {} with type: {}", accountId, type);
        
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        List<Transaction> transactions = transactionService.getTransactionsByAccountIdAndType(accountId, type);
        List<TransactionResponse> responses = transactions.stream()
            .map(TransactionResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}/status/{status}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountAndStatus(
            @PathVariable String accountId,
            @PathVariable TransactionStatus status) {
        logger.info("Fetching transactions for account: {} with status: {}", accountId, status);
        
        if (status == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
        
        List<Transaction> transactions = transactionService.getTransactionsByAccountIdAndStatus(accountId, status);
        List<TransactionResponse> responses = transactions.stream()
            .map(TransactionResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable UUID id,
            @RequestBody UpdateTransactionRequest request) {
        logger.info("Updating transaction: {}", id);
        
        return transactionService.getTransactionById(id)
            .map(existing -> {
                if (request.status() != null) {
                    Transaction updated = existing.withStatus(request.status());
                    Transaction saved = transactionService.updateTransaction(updated);
                    logger.info("Transaction updated successfully: {}", id);
                    return ResponseEntity.ok(TransactionResponse.fromDomain(saved));
                }
                return ResponseEntity.ok(TransactionResponse.fromDomain(existing));
            })
            .orElseGet(() -> {
                logger.warn("Transaction not found for update: {}", id);
                return ResponseEntity.notFound().build();
            });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        logger.info("Deleting transaction: {}", id);
        
        return transactionService.getTransactionById(id)
            .map(transaction -> {
                transactionService.deleteTransaction(id);
                logger.info("Transaction deleted successfully: {}", id);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElseGet(() -> {
                logger.warn("Transaction not found for deletion: {}", id);
                return ResponseEntity.notFound().build();
            });
    }
}