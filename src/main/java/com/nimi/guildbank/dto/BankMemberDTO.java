package com.nimi.guildbank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimi.guildbank.domain.Bank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

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
