package com.nimi.guildbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
public class CreateAccountResponse {
    private final long id;
    private final long bankId;
    private final Double accountAmount;
}
