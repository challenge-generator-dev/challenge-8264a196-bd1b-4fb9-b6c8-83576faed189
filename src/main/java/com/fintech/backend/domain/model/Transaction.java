package com.fintech.backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(
    UUID id,
    String accountId,
    BigDecimal amount,
    TransactionType type,
    LocalDateTime timestamp,
    TransactionStatus status,
    String reference
) {
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, REVERSED
    }

    public Transaction {
        if (id == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = TransactionStatus.PENDING;
        }
        if (reference == null) {
            reference = "";
        }
    }

    public Transaction withStatus(TransactionStatus newStatus) {
        return new Transaction(this.id, this.accountId, this.amount, this.type, this.timestamp, newStatus, this.reference);
    }

    public boolean isTerminalState() {
        return this.status == TransactionStatus.COMPLETED || this.status == TransactionStatus.FAILED || this.status == TransactionStatus.REVERSED;
    }
}