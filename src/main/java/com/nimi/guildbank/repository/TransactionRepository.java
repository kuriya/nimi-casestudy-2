package com.nimi.guildbank.repository;

import com.nimi.guildbank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for 'Transaction' domain
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
