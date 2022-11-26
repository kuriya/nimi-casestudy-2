package com.nimi.guildbank.controller;

import com.nimi.guildbank.dto.*;
import com.nimi.guildbank.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * This controller handles all requests related to guild bank operations
 */
@Controller
@RequestMapping("bankservice/api/banks")
public final class GuildBankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildBankController.class);

    private final BankService bankService;

    public GuildBankController(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Creating a new Bank
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity<CreateBankResponse> createBank(@Validated @RequestBody final CreateBankRequest request) {
        LOGGER.info("Creating a Guild Bank {}", request.toString());
        final CreateBankResponse response = bankService.createBank(request);
        LOGGER.info("Completing a Guild Bank {}", response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * This method is responsible to create a new bank account based on the bank Id
     * @param request
     * @return
     */
    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(@Validated @RequestBody final CreateAccountRequest request) {
        LOGGER.info("Creating a Bank account {}",request.toString());
        final CreateAccountResponse response = bankService.createAccount(request);
        LOGGER.info("Bank account creation is completed {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method is responsible to fetch all banks with OPEN status
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Collection<BankDTO>> getBanks() {
        LOGGER.info("Fetching Bank request received ");
        final Collection<BankDTO> response = bankService.getBanks();
        LOGGER.info("Completing Fetching Bank request");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is responsible to fetch a bank by Id
     * @return
     */
    @GetMapping("/{bankId}")
    public ResponseEntity<BankDTO> getBankById(@PathVariable long bankId) {
        LOGGER.info("Fetching Bank by bank Id : {}",bankId);
        final BankDTO response = bankService.getBankById(bankId);
        LOGGER.info("Fetched Bank by bank Id : {} Bank : {}",bankId,response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is responsible to fetch a bank account  by bank Id
     * @return
     */
    @GetMapping("/{bankId}/accounts")
    public ResponseEntity<AccountDTO> getAccountByBankId(@PathVariable long bankId) {
        LOGGER.info("Fetching Bank by bank Id : {}",bankId);
        final AccountDTO response = bankService.getAccountByBankId(bankId);
        LOGGER.info("Fetched Bank account by bank Id : {}",bankId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is responsible to fetch a bank account  by bank Id
     * @return
     */
    @GetMapping("/{bankId}/accounts/{accountId}")
    public ResponseEntity<AccountDTO> getAccountByBankIdAndAccountId(@PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Fetching Bank account by bank Id : {} and account id : {}",bankId, accountId);
        final AccountDTO response = bankService.getAccountByBankIdAndAccountId(bankId, accountId);
        LOGGER.info("Fetched Bank account by bank Id : {}",bankId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is responsible to add members to bank
     * @return
     */
    @PostMapping("/{bankId}/members/{userId}")
    public ResponseEntity<AddBankMemberResponse> addMembersToBank(@PathVariable long bankId, @PathVariable String userId) {
        LOGGER.info("Adding member : {} to bank : {}", userId, bankId);
        final AddBankMemberResponse response = bankService.addMembers(bankId, userId);
        LOGGER.info("Added member : {} to bank : {}", userId, bankId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is responsible to get members by bank Id
     * @return
     */
    @GetMapping("/{bankId}/members")
    public ResponseEntity<Collection<BankMemberDTO>> getMembersByBankId(@PathVariable long bankId) {
        LOGGER.info("Fetching members for bank id : {}", bankId);
        final Collection<BankMemberDTO> response = bankService.getMembersByBankId(bankId);
        LOGGER.info("Fetched members for bank id : {}", bankId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * This method is responsible to get members by bank Id and User Id
     * @return
     */
    @GetMapping("/{bankId}/members/{userId}")
    public ResponseEntity<BankMemberDTO> getMemberByBankIdUserId(@PathVariable long bankId, @PathVariable String userId) {
        LOGGER.info("Fetching member by Bank Id : {} and User Id : {}", bankId, userId);
        final BankMemberDTO response = bankService.getMemberByBankIdUserId(bankId, userId);
        LOGGER.info("Fetched member by Bank Id : {} and User Id : {}", bankId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Closing the bank
     * @return
     */
    @PostMapping("/{bankId}/close")
    public ResponseEntity<CloseBankResponse> closeBank(@PathVariable long bankId) {
        LOGGER.info("Request received to close the bank : {}", bankId);
        final CloseBankResponse response = bankService.closeBank(bankId);
        LOGGER.info("Completing a Guild Bank close operation : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Closing the bank account
     * @return
     */
    @PostMapping("/{bankId}/accounts/{accountId}/close")
    public ResponseEntity<CloseAccountResponse> closeAccount(@PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to close the bank account : {}", accountId);
        final CloseAccountResponse response = bankService.closeAccount(bankId,accountId);
        LOGGER.info("Account close operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Deposit
     * @return
     */
    @PostMapping("/{bankId}/accounts/{accountId}/deposit")
    public ResponseEntity<AccountDepositResponse> deposit(@Validated @RequestBody final AccountTransactionRequest request,
                                                        @PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to deposit account : {} and amount : {} ", accountId, request.getTransactionAmount());
        final AccountDepositResponse response = bankService.deposit(bankId,accountId, request);
        LOGGER.info("Account deposit operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Withdrawal
     * @return
     */
    @PostMapping("/{bankId}/accounts/{accountId}/withdraw")
    public ResponseEntity<AccountDepositResponse> withdrawal(@Validated @RequestBody final AccountTransactionRequest request,
                                                          @PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to withdraw from account : {} and amount : {} ", accountId, request.getTransactionAmount());
        final AccountDepositResponse response = bankService.deposit(bankId,accountId, request);
        LOGGER.info("Account withdrawal operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
