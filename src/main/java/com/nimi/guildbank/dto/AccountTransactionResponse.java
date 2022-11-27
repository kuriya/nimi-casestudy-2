package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransactionResponse {
    private final long bankId;
    private final long accountId;
    private final double accountBalance;
    private final double transactionAmount;
}
