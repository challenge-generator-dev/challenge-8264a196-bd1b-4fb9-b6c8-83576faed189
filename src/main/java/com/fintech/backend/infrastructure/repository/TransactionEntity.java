package com.fintech.backend.infrastructure.repository;



import com.fintech.backend.domain.model.TransactionStatus;
import com.fintech.backend.domain.model.TransactionType;
import com.fintech.backend.domain.model.Transaction;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transaction.TransactionType type;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transaction.TransactionStatus status;

    @Column
    private String reference;

    public TransactionEntity() {
    }

    public TransactionEntity(Transaction transaction) {
        this.id = transaction.id();
        this.accountId = transaction.accountId();
        this.amount = transaction.amount();
        this.type = transaction.type();
        this.timestamp = transaction.timestamp();
        this.status = transaction.status();
        this.reference = transaction.reference();
    }

    public Transaction toDomain() {
        return new Transaction(
            this.id,
            this.accountId,
            this.amount,
            this.type,
            this.timestamp,
            this.status,
            this.reference
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transaction.TransactionType getType() {
        return type;
    }

    public void setType(Transaction.TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(Transaction.TransactionStatus status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}