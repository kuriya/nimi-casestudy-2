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

/**
 * This controller handles all requests related to guild bank operations
 */
@Controller
@RequestMapping("bankservice/api")
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
    @PostMapping("/banks")
    public ResponseEntity<CreateBankResponse> createBank(@Validated @RequestBody final CreateBankRequest request) {
        LOGGER.info("Creating a Guild Bank " + request.toString());
        final CreateBankResponse response = bankService.createBank(request);
        LOGGER.info("Completing a Guild Bank " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * This method is responsible to create a new bank account based on the bank Id
     * @param request
     * @return
     */
    @PostMapping("/banks/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(@Validated @RequestBody final CreateAccountRequest request) {
        LOGGER.info("Creating a Bank account" + request.toString());
        final CreateAccountResponse response = bankService.createAccount(request);
        LOGGER.info("Bank account creation is completed " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /*@GetMapping("/banks")
    public ResponseEntity<CreateBankResponse> getBank() {
        LOGGER.info("Creating a Guild Bank " + request.toString());
        final CreateBankResponse response = bankService.createBank(request);
        LOGGER.info("Completing a Guild Bank " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/banks/{id}")
    public ResponseEntity<CreateBankResponse> getBank(@Validated @RequestBody final CreateBankRequest request) {
        LOGGER.info("Creating a Guild Bank " + request.toString());
        final CreateBankResponse response = bankService.createBank(request);
        LOGGER.info("Completing a Guild Bank " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }*/

    /**
     * Closing the bank
     * @return
     */
    @PostMapping("/banks/{bankId}/close")
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
    @PostMapping("/banks/{bankId}/accounts/{accountId}/close")
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
    @PostMapping("/banks/{bankId}/accounts/{accountId}/close")
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
    @PostMapping("/banks/{bankId}/accounts/{accountId}/close")
    public ResponseEntity<AccountDepositResponse> withdrawal(@Validated @RequestBody final AccountTransactionRequest request,
                                                          @PathVariable long bankId, @PathVariable long accountId) {
        LOGGER.info("Request received to withdraw from account : {} and amount : {} ", accountId, request.getTransactionAmount());
        final AccountDepositResponse response = bankService.deposit(bankId,accountId, request);
        LOGGER.info("Account withdrawal operation is completed : {}",response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
