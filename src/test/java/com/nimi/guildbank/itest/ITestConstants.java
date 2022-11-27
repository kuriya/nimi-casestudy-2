package com.nimi.guildbank.itest;

/**
 * This class represents useful constants for tests
 */
public class ITestConstants {
    static final String GUILD_BANK_RESOURCE = "bankservice/api/banks";
    static final String GUILD_BANK_ACCOUNT_RESOURCE = "bankservice/api/banks/{bankId}/accounts";
    static final String GUILD_BANK_ACCOUNT_CLOSE_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/close";
    static final String GUILD_BANK_MEMBER_USER_RESOURCE = "bankservice/api/banks/{bankId}/members/{userId}";
    static final String GUILD_BANK_MEMBER_RESOURCE = "bankservice/api/banks/{bankId}/members";
    static final String GUILD_BANK_DEPOSIT_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/deposit";
    static final String GUILD_BANK_WITHDRAW_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/withdraw";
    static final String GUILD_BANK_CLOSE_RESOURCE = "bankservice/api/banks/{bankId}/close";
    static final String ID = "id";
    static final String BALANCE = "balance";
    static final String BANK_NAME = "TestBank";
    static final String USER_ID_1 = "5620071b-5cd6-4468-97ab-744ab1f3e010";
    static final String USER_ID_2 = "5720071b-5cd6-4468-97ab-744ab1f3e011";
    static final double DEPOSIT_AMOUNT = 10000;
    static final double TRANSACTION_AMOUNT = 5000;
    static final double TRANSACTION_AMOUNT_ = 15000;
}
