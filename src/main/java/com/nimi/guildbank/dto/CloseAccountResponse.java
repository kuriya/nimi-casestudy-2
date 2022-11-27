package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloseAccountResponse {
    private final AccountStatus status;
    private final long id;
    private final long bankId;
    private final double balance;
}
