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
public class CreateBankResponse {
    private final String name;
    private final long id;
    private final String creatorId;
}
