package com.nimi.guildbank.service;

import com.nimi.guildbank.dto.*;

import java.util.Collection;

public interface BankService {

    CreateBankResponse createBank(CreateBankRequest request);

    CloseBankResponse closeBank(long id);

    CreateAccountResponse createAccount(CreateAccountRequest request);

    CloseAccountResponse closeAccount(long bankId, long accountId);

    AccountDepositResponse deposit(long bankId, long accountId, AccountTransactionRequest request);

    AccountDepositResponse withdrawal(long bankId, long accountId, AccountTransactionRequest request);

    Collection<BankDTO> getBanks();

    BankDTO getBankById(long bankId);

    AccountDTO getAccountByBankId(long bankId);

    AccountDTO getAccountByBankIdAndAccountId(long bankId, long accountId);

    AddBankMemberResponse addMembers(long bankId, String userId);

    Collection<BankMemberDTO> getMembersByBankId(long bankId);

    BankMemberDTO getMemberByBankIdUserId(long bankId, String userId);



}
