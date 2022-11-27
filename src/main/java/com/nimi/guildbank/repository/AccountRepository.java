package com.nimi.guildbank.repository;

import com.nimi.guildbank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for Account domain
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
