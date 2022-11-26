package com.nimi.guildbank.repository;

import com.nimi.guildbank.domain.Account;
import com.nimi.guildbank.domain.BankMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankMemberRepository extends JpaRepository<BankMember, Long> {

}
