package com.nimi.guildbank.repository;

import com.nimi.guildbank.domain.Bank;
import com.nimi.guildbank.domain.BankStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Collection<Bank> findByStatus(BankStatus status);

}
