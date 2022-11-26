package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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
@Builder
public class AddBankMemberResponse {
    private final String userId;
    private final long bankId;
    private final long memberId;
}
