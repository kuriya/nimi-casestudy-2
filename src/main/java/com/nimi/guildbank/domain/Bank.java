package com.nimi.guildbank.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * This class represents 'banks' table in the database.
 * All created new Guild banks are saved in this table
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    @Enumerated(EnumType.STRING)
    private BankStatus status;

    @Column(name = "creator_id")
    private String creatorId;

    private String name;

    @Setter
    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
    private Collection<BankMember> members;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
