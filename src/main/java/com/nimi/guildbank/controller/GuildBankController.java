package com.nimi.guildbank.controller;

import com.nimi.guildbank.dto.CreateBankRequest;
import com.nimi.guildbank.dto.CreateBankResponse;
import com.nimi.guildbank.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("bankservice/api")
public final class GuildBankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildBankController.class);

    private final BankService bankService;


    public GuildBankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/banks")
    public ResponseEntity<CreateBankResponse> createBank(@Validated @RequestBody final CreateBankRequest request) {
        LOGGER.info("Creating a Guild Bank " + request.toString());
        final CreateBankResponse response = bankService.createBank(request);
        LOGGER.info("Completing a Guild Bank " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
