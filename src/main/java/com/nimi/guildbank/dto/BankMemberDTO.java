package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankMemberDTO {
    private long memberId;
    private long bankId;
    private LocalDateTime createdAt;
    private String userId;
}
