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
public class CreateBankResponse {
    private final String name;
    private final long id;


    public CreateBankResponse(String name, long id) {
        this.name = name;
        this.id = id;
    }
}
