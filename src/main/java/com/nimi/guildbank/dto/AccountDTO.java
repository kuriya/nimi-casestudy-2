package com.nimi.guildbank.dto;

import com.nimi.guildbank.domain.AccountStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
public class AccountDTO {
    private final long id;
    private double balance;
    private String creatorId;
    private AccountStatus status;
}
