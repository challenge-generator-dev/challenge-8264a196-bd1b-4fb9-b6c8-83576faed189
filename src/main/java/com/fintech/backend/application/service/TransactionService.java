package com.fintech.backend.application.service;

import com.fintech.backend.domain.model.Transaction;
import com.fintech.backend.domain.model.Transaction.TransactionStatus;
import com.fintech.backend.domain.model.Transaction.TransactionType;
import com.fintech.backend.domain.port.TransactionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000.00");
    private static final int MAX_REFERENCE_LENGTH = 100;

    private final TransactionRepositoryPort transactionRepositoryPort;

    public TransactionService(TransactionRepositoryPort transactionRepositoryPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Transactional
    public Transaction createTransaction(String accountId, BigDecimal amount, 
                                          TransactionType type, String reference) {
        logger.info("Iniciando creación de transacción para cuenta: {}, tipo: {}, monto: {}", 
                    accountId, type, amount);

        validateTransactionInput(accountId, amount, type, reference);

        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            amount,
            type,
            LocalDateTime.now(),
            TransactionStatus.PENDING,
            reference
        );

        Transaction savedTransaction = transactionRepositoryPort.save(transaction);
        logger.info("Transacción creada exitosamente con ID: {}", savedTransaction.id());
        
        return savedTransaction;
    }

    @Transactional
    public Transaction processTransaction(UUID transactionId, TransactionStatus newStatus) {
        logger.info("Procesando transacción: {} con nuevo estado: {}", transactionId, newStatus);

        Optional<Transaction> optionalTransaction = transactionRepositoryPort.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            logger.error("Transacción no encontrada: {}", transactionId);
            throw new TransactionNotFoundException("Transacción no encontrada: " + transactionId);
        }

        Transaction transaction = optionalTransaction.get();
        
        if (!isValidStatusTransition(transaction.status(), newStatus)) {
            logger.error("Transición de estado inválida: {} -> {}", transaction.status(), newStatus);
            throw new InvalidStatusTransitionException(
                "Transición inválida de " + transaction.status() + " a " + newStatus);
        }

        Transaction updatedTransaction = transaction.withStatus(newStatus);
        Transaction savedTransaction = transactionRepositoryPort.save(updatedTransaction);
        
        logger.info("Transacción procesada exitosamente: {}", savedTransaction.id());
        return savedTransaction;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccount(String accountId) {
        logger.debug("Consultando transacciones para cuenta: {}", accountId);
        
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("El ID de cuenta no puede estar vacío");
        }

        List<Transaction> transactions = transactionRepositoryPort.findByAccountId(accountId);
        logger.debug("Encontradas {} transacciones para cuenta: {}", transactions.size(), accountId);
        
        return transactions;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountAndType(String accountId, TransactionType type) {
        logger.debug("Consultando transacciones para cuenta: {} y tipo: {}", accountId, type);
        
        validateAccountId(accountId);
        
        if (type == null) {
            throw new IllegalArgumentException("El tipo de transacción no puede ser nulo");
        }

        return transactionRepositoryPort.findByAccountIdAndType(accountId, type);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountAndStatus(String accountId, TransactionStatus status) {
        logger.debug("Consultando transacciones para cuenta: {} y estado: {}", accountId, status);
        
        validateAccountId(accountId);
        
        if (status == null) {
            throw new IllegalArgumentException("El estado de transacción no puede ser nulo");
        }

        return transactionRepositoryPort.findByAccountIdAndStatus(accountId, status);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        logger.debug("Consultando transacciones entre {} y {}", start, end);
        
        if (start == null || end == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        List<Transaction> transactions = transactionRepositoryPort.findByTimestampBetween(start, end);
        logger.debug("Encontradas {} transacciones en el rango de fechas", transactions.size());
        
        return transactions;
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(UUID id) {
        logger.debug("Consultando transacción por ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("El ID de transacción no puede ser nulo");
        }

        return transactionRepositoryPort.findById(id);
    }

    private void validateTransactionInput(String accountId, BigDecimal amount, 
                                          TransactionType type, String reference) {
        validateAccountId(accountId);
        
        if (amount == null) {
            throw new IllegalArgumentException("El monto de la transacción no puede ser nulo");
        }

        if (amount.compareTo(MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor o igual a " + MIN_AMOUNT);
        }

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                "El monto debe ser menor o igual a " + MAX_AMOUNT);
        }

        if (type == null) {
            throw new IllegalArgumentException("El tipo de transacción no puede ser nulo");
        }

        if (reference != null && reference.length() > MAX_REFERENCE_LENGTH) {
            throw new IllegalArgumentException(
                "La referencia no puede exceder " + MAX_REFERENCE_LENGTH + " caracteres");
        }
    }

    private void validateAccountId(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("El ID de cuenta no puede estar vacío");
        }
    }

    private boolean isValidStatusTransition(TransactionStatus current, TransactionStatus next) {
        return switch (current) {
            case PENDING -> next == TransactionStatus.PROCESSING || 
                             next == TransactionStatus.COMPLETED || 
                             next == TransactionStatus.FAILED;
            case PROCESSING -> next == TransactionStatus.COMPLETED || 
                                next == TransactionStatus.FAILED;
            case COMPLETED -> false;
            case FAILED -> next == TransactionStatus.PENDING;
        };
    }

    public static class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidStatusTransitionException extends RuntimeException {
        public InvalidStatusTransitionException(String message) {
            super(message);
        }
    }
}