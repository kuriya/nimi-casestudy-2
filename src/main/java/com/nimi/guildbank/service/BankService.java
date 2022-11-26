package com.nimi.guildbank.service;

import com.nimi.guildbank.dto.*;

public interface BankService {

    CreateBankResponse createBank(CreateBankRequest request);

    CloseBankResponse closeBank(long id);

    CreateAccountResponse createAccount(CreateAccountRequest request);

    CloseAccountResponse closeAccount(final long bankId, final long accountId);

    AccountDepositResponse deposit(final long bankId, final long accountId, AccountTransactionRequest request);

    AccountDepositResponse withdrawal(final long bankId, final long accountId, AccountTransactionRequest request);

}
