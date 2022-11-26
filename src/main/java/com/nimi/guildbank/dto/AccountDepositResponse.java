package com.nimi.guildbank.dto;

import com.nimi.guildbank.domain.BankStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
public class AccountDepositResponse {
    private final long bankId;
    private final long accountId;
    private final double accountBalance;
    private final double transactionAmount;



}