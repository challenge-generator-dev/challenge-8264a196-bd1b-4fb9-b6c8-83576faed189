package com.fintech.backend.infrastructure.repository;



import com.fintech.backend.domain.model.TransactionStatus;
import com.fintech.backend.domain.model.TransactionType;
import com.fintech.backend.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByAccountId(String accountId);
    
    List<TransactionEntity> findByAccountIdAndType(String accountId, Transaction.TransactionType type);
    
    List<TransactionEntity> findByAccountIdAndStatus(String accountId, Transaction.TransactionStatus status);
    
    List<TransactionEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}