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
public class AccountTransactionRequest {



    @NotNull(message = "'transactionAmount' should not be null")
    @PositiveOrZero
    private final Double transactionAmount;

    @NotBlank(message = "'creatorId' should not be empty")
    private final String creatorId;


    @JsonCreator
    public AccountTransactionRequest(@JsonProperty("transactionAmount") Double transactionAmount, @JsonProperty("creatorId") String creatorId) {
        this.transactionAmount = transactionAmount;
        this.creatorId = creatorId;
    }


}
