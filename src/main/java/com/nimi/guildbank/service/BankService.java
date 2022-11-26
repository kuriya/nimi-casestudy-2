package com.nimi.guildbank.service;

import com.nimi.guildbank.dto.CreateBankRequest;
import com.nimi.guildbank.dto.CreateBankResponse;

public interface BankService {

    CreateBankResponse createBank(CreateBankRequest request);
}
