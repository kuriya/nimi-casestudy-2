package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@ToString
public class CreateAccountRequest {

    @NotNull(message = "'depositAmount' should not be null")
    @PositiveOrZero
    private final Double depositAmount;

    @NotBlank(message = "'creatorId' should not be empty")
    private final String creatorId;


    @JsonCreator
    public CreateAccountRequest(@JsonProperty("depositAmount") Double depositAmount, @JsonProperty("creatorId") String creatorId) {
        this.depositAmount = depositAmount;
        this.creatorId = creatorId;
    }


}
