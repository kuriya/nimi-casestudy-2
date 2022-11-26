package com.nimi.guildbank.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts")
public class Account {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "creator_id")
    private String creatorId;

    @Setter
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Setter
    private double amount;

    @OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Bank bank;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Collection<Transaction> transactions;

    @Column(name = "created_at", insertable = true, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private long version;
}
