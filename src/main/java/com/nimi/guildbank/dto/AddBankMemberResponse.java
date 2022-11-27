package com.nimi.guildbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
