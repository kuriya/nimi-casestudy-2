package com.nimi.guildbank.service.impl;

import com.nimi.guildbank.domain.Bank;
import com.nimi.guildbank.dto.CreateBankRequest;
import com.nimi.guildbank.dto.CreateBankResponse;
import com.nimi.guildbank.repository.BankRepository;
import com.nimi.guildbank.service.BankService;
import com.nimi.guildbank.transformer.BankToCreateBankResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankServiceImpl.class);
    private final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public CreateBankResponse createBank(final CreateBankRequest request) {
        final Bank bank = Bank.builder().name(request.getName()).creatorId(request.getCreatorId()).build();
        final Bank savedBank = bankRepository.save(bank);
        return new BankToCreateBankResponseTransformer().transform(savedBank);
    }
}
