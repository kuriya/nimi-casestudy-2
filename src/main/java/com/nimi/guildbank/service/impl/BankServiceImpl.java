package com.nimi.guildbank.service.impl;

import com.nimi.guildbank.domain.*;
import com.nimi.guildbank.dto.*;
import com.nimi.guildbank.repository.AccountRepository;
import com.nimi.guildbank.repository.BankMemberRepository;
import com.nimi.guildbank.repository.BankRepository;
import com.nimi.guildbank.service.BankService;
import com.nimi.guildbank.transformer.AccountToAccountDTOTransformer;
import com.nimi.guildbank.transformer.BankToBankDTOTransformer;
import com.nimi.guildbank.transformer.BankToCreateBankResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * This service class represents all bank related business logics
 */

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankServiceImpl.class);
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final BankMemberRepository bankMemberRepository;

    public BankServiceImpl(BankRepository bankRepository, AccountRepository accountRepository,
                           BankMemberRepository bankMemberRepository) {
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
        this.bankMemberRepository = bankMemberRepository;
    }

    /**
     * This method will create and save a new guild bank
     * @param request
     * @return
     */
    @Override
    public CreateBankResponse createBank(final CreateBankRequest request) {
        final Bank bank = Bank.builder().name(request.getName()).creatorId(request.getCreatorId()).status(BankStatus.OPEN).build();
        final BankMember member =  BankMember.builder().userId(request.getCreatorId()).bank(bank).build();
        bank.setMembers(Collections.singletonList(member));
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
     *
     * @param bankId
     * @param request
     * @return
     */
    @Override
    public CreateAccountResponse createAccount(long bankId, final CreateAccountRequest request) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));
        if (bank.getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        final Account account = Account.builder().bank(bank).creatorId(request.getCreatorId()).status(AccountStatus.OPEN).amount(request.getDepositAmount()).build();
        bank.setAccount(account);
        final Account savedAccount = accountRepository.save(account);

        return CreateAccountResponse.builder().balance(savedAccount.getAmount())
                .bankId(savedAccount.getBank().getId()).creatorId(savedAccount.getCreatorId()).id(savedAccount.getId()).build();

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
                .bankId(savedAccount.getBank().getId()).balance(savedAccount.getAmount()).build();

    }

    /**
     * This method is responsible to handle the business logic of deposit operation
     * @param accountId
     * @return
     */
    @Override
    public AccountTransactionResponse deposit(final long bankId, final long accountId, final AccountTransactionRequest request) {
        final Account account = getBankAccount(bankId, accountId);

        double accountBalance = account.getAmount() + request.getTransactionAmount();
        account.setAmount(accountBalance);

        final Transaction transaction = Transaction.builder().account(account).type(TransactionType.CR).creatorId(request.getCreatorId())
                .amount(request.getTransactionAmount()).build();

        account.getTransactions().add(transaction);

        if (account.getBank().getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        if (account.getStatus() == AccountStatus.CLOSE) {
            throw new IllegalArgumentException("Account is already closed");
        }

        final Account savedAccount = accountRepository.save(account);

        return AccountTransactionResponse.builder().accountBalance(savedAccount.getAmount())
                .transactionAmount(request.getTransactionAmount()).bankId(bankId).accountId(accountId).build();

    }

    /**
     * This method is responsible to handle the business logic of deposit operation
     * @param accountId
     * @return
     */
    @Override
    public AccountTransactionResponse withdrawal(final long bankId, final long accountId, final AccountTransactionRequest request) {
        final Account account = getBankAccount(bankId, accountId);
        if (request.getTransactionAmount() > account.getAmount()) {
            throw new IllegalArgumentException("Account balance is not sufficient");
        }
        double accountBalance = account.getAmount() - request.getTransactionAmount();
        account.setAmount(accountBalance);

        final Transaction transaction = Transaction.builder().account(account).type(TransactionType.DR).creatorId(request.getCreatorId())
                .amount(request.getTransactionAmount()).build();

        account.getTransactions().add(transaction);

        if (account.getBank().getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }
        if (account.getStatus() == AccountStatus.CLOSE) {
            throw new IllegalArgumentException("Account is already closed");
        }
        final Account savedAccount = accountRepository.save(account);

        return AccountTransactionResponse.builder().accountBalance(savedAccount.getAmount())
                .transactionAmount(request.getTransactionAmount()).bankId(bankId).accountId(accountId).build();

    }

    /**
     * Getting bank account using account Id and validating with the bank
     * @param bankId
     * @param accountId
     * @return
     */
    private Account getBankAccount(final long bankId, final long accountId) {
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

    /**
     * Get active banks
     * @return
     */
    @Override
    public Collection<BankDTO> getBanks() {
        return bankRepository.findByStatus(BankStatus.OPEN).stream().map(bank -> new BankToBankDTOTransformer()
                .transform(bank)).collect(Collectors.toList());
    }

    /**
     * Fetch bank by bank Id
     * @param bankId
     * @return
     */
    @Override
    public BankDTO getBankById(final long bankId) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));
        return new BankToBankDTOTransformer().transform(bank);
    }

    /**
     * Fetch account by bank Id
     * @param bankId
     * @return
     */
    @Override
    public AccountDTO getAccountByBankId(final long bankId){
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        return new AccountToAccountDTOTransformer().transform(bank.getAccount());
    }

    /**
     * Fetch account by bank id and account id
     * @param bankId
     * @param accountId
     */
    @Override
    public AccountDTO getAccountByBankIdAndAccountId(final long bankId, final long accountId) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        if (bank.getAccount() == null) {
            throw new IllegalArgumentException("No Bank accounts found for this bank id : " + bankId);
        } else if (bank.getAccount().getId() != accountId) {
            throw new IllegalArgumentException("Bank and Bank account mismatch");
        }
        return new AccountToAccountDTOTransformer().transform(bank.getAccount());
    }

    /**
     * Adding a new member to the Bank
     * @param bankId
     * @param userId
     * @return
     */
    @Override
    public AddBankMemberResponse addMembers(final long bankId, final String userId) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));
        if (bank.getStatus() == BankStatus.CLOSE) {
            throw new IllegalArgumentException("Bank is already closed");
        }

        //validity checking member has already membership
        if(bank.getMembers().stream().anyMatch(m -> m.getUserId().equals(userId))){
            throw new IllegalArgumentException("User : "+userId+" has already membership with bank : "+bankId);
        }

        final BankMember member = BankMember.builder().bank(bank).userId(userId).build();
        bank.getMembers().add(member);
        final BankMember savedMember = bankMemberRepository.save(member);
        return AddBankMemberResponse.builder().memberId(savedMember.getId()).userId(savedMember.getUserId()).bankId(bankId).build();
    }

    /**
     * Fetching all members in the Bank
     * @param bankId
     * @return
     */
    @Override
    public Collection<BankMemberDTO> getMembersByBankId(final long bankId) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        return bank.getMembers().stream().map(member -> BankMemberDTO.builder().bankId(bank.getId())
                .memberId(member.getId()).userId(member.getUserId()).createdAt(member.getCreatedAt()).build()).collect(Collectors.toList());
    }

    /**
     * Fetching member by Bank Id and User Id
     * @param bankId
     * @param userId
     * @return
     */
    @Override
    public BankMemberDTO getMemberByBankIdUserId(final long bankId, final String userId) {
        final Bank bank = bankRepository.findById(bankId).orElseThrow(() ->
                new InvalidConfigurationPropertyValueException("BankId", "Bank", "the bank does not exist for the given Id"));

        return bank.getMembers().stream().filter(m -> m.getUserId().equals(userId)).map(member -> BankMemberDTO.builder().bankId(bank.getId())
                        .memberId(member.getId()).userId(member.getUserId()).createdAt(member.getCreatedAt()).build())
                .collect(Collectors.toList()).stream().findFirst().orElseThrow(() -> new InvalidConfigurationPropertyValueException("MemberId", "Member", "the bank member not exist for the given user Id"));

    }
}
