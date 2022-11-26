package com.nimi.guildbank.service.impl;

import com.nimi.guildbank.domain.*;
import com.nimi.guildbank.dto.*;
import com.nimi.guildbank.repository.AccountRepository;
import com.nimi.guildbank.repository.BankRepository;
import com.nimi.guildbank.service.BankService;
import com.nimi.guildbank.transformer.BankToCreateBankResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * This service class represents all bank related business logics
 */

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankServiceImpl.class);
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;

    public BankServiceImpl(BankRepository bankRepository, AccountRepository accountRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * This method will create and save a new guild bank
     * @param request
     * @return
     */
    @Override
    public CreateBankResponse createBank(final CreateBankRequest request) {
        final Bank bank = Bank.builder().name(request.getName()).creatorId(request.getCreatorId()).status(BankStatus.OPEN).build();
        final Bank savedBank = bankRepository.save(bank);
        return new BankToCreateBankResponseTransformer().transform(savedBank);
    }

    /**
     * This method will close the guild bank, then operation will be stopped
     * @return
     */
    @Override
    public CloseBankResponse closeBank(final long id) {
        final Bank bank = bankRepository.findById(id).orElseThrow(() -> new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));
        if (bank.getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        bank.setStatus(BankStatus.CLOSE);
        final Bank savedBank = bankRepository.save(bank);
        return CloseBankResponse.builder().id(savedBank.getId()).status(savedBank.getStatus()).build();
    }

    /**
     * This method is responsible to handle business logic of account create operation
     * @param request
     * @return
     */
    @Override
    public CreateAccountResponse createAccount(final CreateAccountRequest request) {
        final Bank bank = bankRepository.findById(request.getBankId()).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));
        if (bank.getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        final Account account = Account.builder().bank(bank).creatorId(request.getCreatorId()).status(AccountStatus.OPEN).amount(request.getDepositAmount()).build();
        final Account savedAccount = accountRepository.save(account);

        return CreateAccountResponse.builder().accountAmount(savedAccount.getAmount())
                .bankId(savedAccount.getBank().getId()).id(savedAccount.getId()).build();

    }


    /**
     * This method is responsible to handle business logic of account close operation
     * @param accountId
     * @return
     */
    @Override
    public CloseAccountResponse closeAccount(final long bankId,final long accountId) {
        final Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        if (account.getBank().getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        if (account.getStatus() == AccountStatus.CLOSE) {
            throw new IllegalArgumentException("Account is already closed");
        }
        account.setStatus(AccountStatus.CLOSE);
        final Account savedAccount = accountRepository.save(account);
        return CloseAccountResponse.builder().id(savedAccount.getId())
                .bankId(savedAccount.getBank().getId()).amount(savedAccount.getAmount()).build();

    }

    /**
     * This method is responsible to handle the business logic of deposit operation
     * @param accountId
     * @return
     */
    @Override
    public AccountDepositResponse deposit(final long bankId, final long accountId, final AccountTransactionRequest request) {
        final Account account = getBankAccount(bankId, accountId);

        double accountBalance = account.getAmount() + request.getTransactionAmount();
        account.setAmount(accountBalance);

        final Transaction transaction = Transaction.builder().account(account).type(TransactionType.CR).creatorId(request.getCreatorId())
                .amount(request.getTransactionAmount()).build();

        account.getTransactions().add(transaction);
        final Account savedAccount = accountRepository.save(account);

        return AccountDepositResponse.builder().accountBalance(savedAccount.getAmount())
                .transactionAmount(request.getTransactionAmount()).bankId(bankId).accountId(accountId).build();

    }

    /**
     * This method is responsible to handle the business logic of deposit operation
     * @param accountId
     * @return
     */
    @Override
    public AccountDepositResponse withdrawal(final long bankId, final long accountId, final AccountTransactionRequest request) {
        final Account account = getBankAccount(bankId, accountId);
        if (request.getTransactionAmount() > account.getAmount()) {
            throw new IllegalArgumentException("Account balance is not sufficient");
        }
        double accountBalance = account.getAmount() - request.getTransactionAmount();
        account.setAmount(accountBalance);

        final Transaction transaction = Transaction.builder().account(account).type(TransactionType.DR).creatorId(request.getCreatorId())
                .amount(request.getTransactionAmount()).build();

        account.getTransactions().add(transaction);
        final Account savedAccount = accountRepository.save(account);

        return AccountDepositResponse.builder().accountBalance(savedAccount.getAmount())
                .transactionAmount(request.getTransactionAmount()).bankId(bankId).accountId(accountId).build();

    }

    /**
     * Getting bank account using account Id and validating with the bank
     * @param bankId
     * @param accountId
     * @return
     */
    private Account getBankAccount(long bankId, long accountId) {
        final Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        if (account.getBank().getId() != bankId) {
            throw new IllegalArgumentException("Bank is not valid");
        }

        if (account.getBank().getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        if (account.getStatus() == AccountStatus.CLOSE) {
            throw new IllegalArgumentException("Account is already closed");
        }

        return account;
    }
}
