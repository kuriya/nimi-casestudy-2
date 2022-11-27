package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddBankMemberResponse {
    private final String userId;
    private final long bankId;
    private final long memberId;
}
