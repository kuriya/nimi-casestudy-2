package com.nimi.guildbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * This class represents request object to create a Guild Bank
 */
@Getter
@Builder
@ToString
public class BankMemberDTO {
    private long memberId;
    private long bankId;
    private LocalDateTime createdAt;
    private String userId;
}
