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
public class BankDTO {
    private long bankId;
    private String creatorId;
    private String name;
    private AccountDTO account;
}
