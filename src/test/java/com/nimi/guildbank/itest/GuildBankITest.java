package com.nimi.guildbank.itest;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.nimi.guildbank.dto.AccountTransactionRequest;
import com.nimi.guildbank.dto.BankMemberDTO;
import com.nimi.guildbank.dto.CreateAccountRequest;
import com.nimi.guildbank.dto.CreateBankRequest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GuildBankITest {

    @LocalServerPort
    private int serverPort;

    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    /**
     * When create bank with valid name and userId
     * Then bank should be created successfully
     */
    @Test
    public void testBankWithValidDetails() {
        final CreateBankRequest request = new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1);
        given().body(request).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED)
                .body("name", is(ITestConstants.BANK_NAME))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("id", Matchers.notNullValue());
    }

    /**
     * When close bank with valid name and userId
     * Then bank should be closed successfully
     */
    @Test
    public void testCloseBankWithValidDetails() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));
    }

    /**
     * When close already closed bank
     * Then throw errors
     */
    @Test
    public void testCloseAlreadyClosedBank() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * When create bank account with valid details
     * Then bank account should be created successfully
     */
    @Test
    public void testCreateBankAccountWithValidDetails() {
        //Creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue());
    }

    /**
     * When close bank account with valid details
     * Then bank account should be closed successfully
     */
    @Test
    public void testCloseBankAccountWithValidDetails() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE, bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int) bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);
        final double balance = JsonPath.from(guildAccountSaveResponse).getDouble(ITestConstants.BALANCE);

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int) bankId))
                .body("balance", equalTo((float) balance))
                .body("id", equalTo((int) accountId));
    }


    /**
     * When close bank account for already closed bank
     * Then throw errors
     */
    @Test
    public void testCloseAlreadyCloseBankAccount() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE, bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int) bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);
        final double balance = JsonPath.from(guildAccountSaveResponse).getDouble(ITestConstants.BALANCE);

        //closing bank
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }


    /**
     * When adding members to bank
     * Then function should work successfully
     */
    @Test
    public void testAddMembersToBank() {
        //Creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //adding member account
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_MEMBER_USER_RESOURCE,bankId, ITestConstants.USER_ID_2).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("userId", is(ITestConstants.USER_ID_2))
                .body("memberId", Matchers.notNullValue());
    }

    /**
     * When deposit amount to bank account
     * Then function should work successfully with correct account balance
     */
    @Test
    public void testDepositToBankAccount() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //deposit 5000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_DEPOSIT_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int)bankId))
                .body("accountId", equalTo((int)accountId))
                .body("accountBalance", equalTo(15000f))
                .body("transactionAmount", equalTo((float) ITestConstants.TRANSACTION_AMOUNT));
    }

    /**
     * When withdraw amount from bank account
     * Then function should work successfully with correct account balance
     */
    @Test
    public void testWithdrawFromBankAccount() {
        //creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //withdraw 5000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_WITHDRAW_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int)bankId))
                .body("accountId", equalTo((int)accountId))
                .body("accountBalance", equalTo(5000f))
                .body("transactionAmount", equalTo((float) ITestConstants.TRANSACTION_AMOUNT));
    }

    /**
     * When deposit to close bank account
     * Then throw error
     */
    @Test
    public void testDepositToCloseBankAccount() {
        //Creating a guild bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int) bankId))
                .body("id", equalTo((int) accountId));

        //deposit 5000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_DEPOSIT_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * When deposit to close bank
     * Then throw error
     */
    @Test
    public void testDepositToCloseBank() {
        //Creating a guild bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //closing bank
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        //deposit 5000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_DEPOSIT_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * When withdraw with insufficient balance
     * Then throw error
     */
    @Test
    public void testWithdrawFromBankAccountWithInsufficientBalance() {
        //creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE, bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int) bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //withdraw 15000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT_, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_WITHDRAW_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * When withdraw from closed bank
     * Then throw error
     */
    @Test
    public void testWithdrawFromCloseBank() {
        //Creating a guild bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //closing bank
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        //withdraw 500
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_WITHDRAW_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    /**
     * When withdraw from closed bank account
     * Then throw error
     */
    @Test
    public void testWithdrawFromCloseBankAccount() {
        //Creating a guild bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(ITestConstants.DEPOSIT_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(ITestConstants.USER_ID_1))
                .body("balance", equalTo((float) ITestConstants.DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ITestConstants.ID);

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int) bankId))
                .body("id", equalTo((int) accountId));

        //withdraw 5000
        given().body(new AccountTransactionRequest(ITestConstants.TRANSACTION_AMOUNT, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_WITHDRAW_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    /**
     * When create a guild bank
     * Then creator should be a member of the bank
     */
    @Test
    public void testWhenCreateBankCreatorShouldBeAMember() {
        //creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(ITestConstants.BANK_NAME, ITestConstants.USER_ID_1)).contentType(ContentType.JSON)
                .when().post(ITestConstants.GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ITestConstants.ID);

        //Fetching members by bank id
        BankMemberDTO[] members = given().contentType(ContentType.JSON)
                .when().get(ITestConstants.GUILD_BANK_MEMBER_RESOURCE, bankId).then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(BankMemberDTO[].class);

        Assert.assertEquals(1, members.length);
        Assert.assertEquals(bankId, members[0].getBankId());
        Assert.assertEquals(ITestConstants.USER_ID_1, members[0].getUserId());
    }
}
