package com.fintech.backend.domain.port;



import com.fintech.backend.domain.model.TransactionStatus;
import com.fintech.backend.domain.model.TransactionType;
import com.fintech.backend.domain.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    
    Optional<Transaction> findById(UUID id);
    
    List<Transaction> findByAccountId(String accountId);
    
    List<Transaction> findByAccountIdAndType(String accountId, Transaction.TransactionType type);
    
    List<Transaction> findByAccountIdAndStatus(String accountId, Transaction.TransactionStatus status);
    
    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}