package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloseBankResponse {
    private final BankStatus status;
    private final long id;
}
