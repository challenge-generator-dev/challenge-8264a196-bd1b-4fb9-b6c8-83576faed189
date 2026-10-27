package com.fintech.backend.infrastructure.adapter;

import com.fintech.backend.domain.model.Transaction;
import com.fintech.backend.domain.model.Transaction.TransactionStatus;
import com.fintech.backend.domain.model.Transaction.TransactionType;
import com.fintech.backend.domain.port.TransactionRepositoryPort;
import com.fintech.backend.infrastructure.repository.TransactionEntity;
import com.fintech.backend.infrastructure.repository.TransactionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TransactionJpaAdapter implements TransactionRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(TransactionJpaAdapter.class);

    private final TransactionJpaRepository transactionJpaRepository;

    public TransactionJpaAdapter(TransactionJpaRepository transactionJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        logger.debug("Guardando transacción: {}", transaction.id());
        
        TransactionEntity entity = mapToEntity(transaction);
        TransactionEntity savedEntity = transactionJpaRepository.save(entity);
        
        logger.debug("Transacción guardada exitosamente: {}", savedEntity.getId());
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        logger.debug("Buscando transacción por ID: {}", id);
        
        Optional<TransactionEntity> entity = transactionJpaRepository.findById(id);
        
        return entity.map(this::mapToDomain);
    }

    @Override
    public List<Transaction> findByAccountId(String accountId) {
        logger.debug("Buscando transacciones por accountId: {}", accountId);
        
        List<TransactionEntity> entities = transactionJpaRepository.findByAccountId(accountId);
        
        logger.debug("Encontradas {} transacciones para accountId: {}", entities.size(), accountId);
        return entities.stream()
                       .map(this::mapToDomain)
                       .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByAccountIdAndType(String accountId, TransactionType type) {
        logger.debug("Buscando transacciones por accountId: {} y tipo: {}", accountId, type);
        
        List<TransactionEntity> entities = transactionJpaRepository.findByAccountIdAndType(
            accountId, type);
        
        logger.debug("Encontradas {} transacciones para accountId: {} y tipo: {}", 
                     entities.size(), accountId, type);
        return entities.stream()
                       .map(this::mapToDomain)
                       .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByAccountIdAndStatus(String accountId, TransactionStatus status) {
        logger.debug("Buscando transacciones por accountId: {} y estado: {}", accountId, status);
        
        List<TransactionEntity> entities = transactionJpaRepository.findByAccountIdAndStatus(
            accountId, status);
        
        logger.debug("Encontradas {} transacciones para accountId: {} y estado: {}", 
                     entities.size(), accountId, status);
        return entities.stream()
                       .map(this::mapToDomain)
                       .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
        logger.debug("Buscando transacciones entre {} y {}", start, end);
        
        List<TransactionEntity> entities = transactionJpaRepository.findByTimestampBetween(start, end);
        
        logger.debug("Encontradas {} transacciones en el rango de fechas", entities.size());
        return entities.stream()
                       .map(this::mapToDomain)
                       .collect(Collectors.toList());
    }

    private TransactionEntity mapToEntity(Transaction transaction) {
        return new TransactionEntity(
            transaction.id(),
            transaction.accountId(),
            transaction.amount(),
            transaction.type(),
            transaction.timestamp(),
            transaction.status(),
            transaction.reference()
        );
    }

    private Transaction mapToDomain(TransactionEntity entity) {
        return new Transaction(
            entity.getId(),
            entity.getAccountId(),
            entity.getAmount(),
            entity.getType(),
            entity.getTimestamp(),
            entity.getStatus(),
            entity.getReference()
        );
    }
}