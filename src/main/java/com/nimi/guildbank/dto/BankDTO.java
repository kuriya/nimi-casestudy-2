package com.nimi.guildbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
public class BankDTO {
    private long bankId;
    private String creatorId;
    private String name;
    private AccountDTO account;

}
