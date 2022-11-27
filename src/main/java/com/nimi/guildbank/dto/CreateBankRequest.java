package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@ToString
public class CreateBankRequest {
    @NotBlank(message = "Bank name should not be empty")
    private final String name;

    @NotBlank(message = "'creatorId' should not be empty")
    private final String creatorId;

    @JsonCreator
    public CreateBankRequest(@JsonProperty("name") String name,
                             @JsonProperty("creatorId") String creatorId) {
        this.name = name;
        this.creatorId = creatorId;
    }

}
