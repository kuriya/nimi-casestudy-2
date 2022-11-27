package com.nimi.guildbank.controller;

import com.nimi.guildbank.dto.*;
import com.nimi.guildbank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Guild Bank Application")
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
    @Operation(summary = "Creating a new Guild Bank for players")
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
    @Operation(summary = "Creating a new Account for an existing Guild Bank")
    @PostMapping("/{bankId}/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(@PathVariable long bankId,
                                                               @Validated @RequestBody final CreateAccountRequest request) {
        LOGGER.info("Creating a Bank account {}",request.toString());
        final CreateAccountResponse response = bankService.createAccount(bankId,request);
        LOGGER.info("Bank account creation is completed {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method is responsible to fetch all banks with OPEN status
     * @return
     */
    @Operation(summary = "Fetching all opened Guild Banks")
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
    @Operation(summary = "Fetching Guild Bank by Bank Id")
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
    @Operation(summary = "Fetching Guild Bank account by Bank Id")
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
    @Operation(summary = "Fetching Guild Bank account by Account Id")
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
    @Operation(summary = "Adding a new member to the Guild Bank")
    @PostMapping("/{bankId}/members/{userId}")
    public ResponseEntity<AddBankMemberResponse> addMembersToBank(@PathVariable long bankId, @PathVariable String userId) {
        LOGGER.info("Adding member : {} to bank : {}", userId, bankId);
        final AddBankMemberResponse response = bankService.addMembers(bankId, userId);
        LOGGER.info("Added member : {} to bank : {}", userId, bankId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method is responsible to get members by bank Id
     * @return
     */
    @Operation(summary = "Fetching all Guild Bank members by bank id")
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
    @Operation(summary = "Fetching Guild Bank member by bank id and user id")
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
    @Operation(summary = "Closing the Bank")
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
    @Operation(summary = "Closing the Bank Account")
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
    @Operation(summary = "Deposit Gold to a Bank Account")
    @PostMapping("/{bankId}/accounts/{accountId}/deposit")
    public ResponseEntity<AccountTransactionResponse> deposit(@Validated @RequestBody final AccountTransactionRequest request,
                                                              @PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to deposit account : {} and amount : {} ", accountId, request.getTransactionAmount());
        final AccountTransactionResponse response = bankService.deposit(bankId,accountId, request);
        LOGGER.info("Account deposit operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Withdrawal
     * @return
     */
    @Operation(summary = "Withdraw gold from Bank Account")
    @PostMapping("/{bankId}/accounts/{accountId}/withdraw")
    public ResponseEntity<AccountTransactionResponse> withdrawal(@Validated @RequestBody final AccountTransactionRequest request,
                                                                 @PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to withdraw from account : {} and amount : {} ", accountId, request.getTransactionAmount());
        final AccountTransactionResponse response = bankService.withdrawal(bankId,accountId, request);
        LOGGER.info("Account withdrawal operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
