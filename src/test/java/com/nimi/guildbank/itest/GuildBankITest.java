package com.nimi.guildbank.itest;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.nimi.guildbank.dto.AccountTransactionRequest;
import com.nimi.guildbank.dto.CreateAccountRequest;
import com.nimi.guildbank.dto.CreateBankRequest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GuildBankITest {

    @LocalServerPort
    private int serverPort;

    private final Gson gson = new Gson();

    private static final String GUILD_BANK_RESOURCE = "bankservice/api/banks";

    private static final String GUILD_BANK_ACCOUNT_RESOURCE = "bankservice/api/banks/{bankId}/accounts";

    private static final String GUILD_BANK_ACCOUNT_CLOSE_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/close";
    private static final String GUILD_BANK_MEMBER_RESOURCE = "bankservice/api/banks/{bankId}/members/{userId}";

    private static final String GUILD_BANK_DEPOSIT_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/deposit";

    private static final String GUILD_BANK_WITHDRAW_RESOURCE = "bankservice/api/banks/{bankId}/accounts/{accountId}/withdraw";

    private static final String GUILD_BANK_CLOSE_RESOURCE = "bankservice/api/banks/{bankId}/close";
    private static final String ID = "id";

    private static final String BALANCE = "balance";
    private static final String BANK_NAME = "TestBank";
    private static final String USER_ID_1 = "5620071b-5cd6-4468-97ab-744ab1f3e010";
    private static final String USER_ID_2 = "5720071b-5cd6-4468-97ab-744ab1f3e011";
    private static final double DEPOSIT_AMOUNT = 10000;

    private static final double TRANSACTION_AMOUNT = 5000;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }


    /**
     * When create bank with valid name and userId
     * Then bank should be created successfully
     */
    /*@Test
    public void testBankWithValidDetails() {
        final CreateBankRequest request = new CreateBankRequest(BANK_NAME, USER_ID_1);
        final String guildBankSaveResponse = given().body(request).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();

        CreateBankResponse response = gson.fromJson(guildBankSaveResponse, CreateBankResponse.class);
        Assert.assertEquals(BANK_NAME,response.getName());
        Assert.assertEquals(BANK_NAME,response.getName());
       // long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);
        System.out.println(response.toString());

    }*/


    /**
     * When create bank with valid name and userId
     * Then bank should be created successfully
     */
    @Test
    public void testBankWithValidDetails() {
        final CreateBankRequest request = new CreateBankRequest(BANK_NAME, USER_ID_1);
        given().body(request).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED)
                .body("name", is(BANK_NAME))
                .body("creatorId", is(USER_ID_1))
                .body("id", Matchers.notNullValue());
    }

    /**
     * When close bank with valid name and userId
     * Then bank should be closed successfully
     */
    @Test
    public void testCloseBankWithValidDetails() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));
    }

    /**
     * When close already closed bank
     * Then throw errors
     */
    @Test
    public void testCloseAlreadyClosedBank() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * When create bank account with valid details
     * Then bank account should be created successfully
     */
    @Test
    public void testCreateBankAccountWithValidDetails() {
        //Creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //Creating account
        given().body(new CreateAccountRequest(DEPOSIT_AMOUNT,USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(USER_ID_1))
                .body("balance", equalTo((float)DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue());
    }

    /**
     * When close bank account with valid details
     * Then bank account should be closed successfully
     */
    @Test
    public void testCloseBankAccountWithValidDetails() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(DEPOSIT_AMOUNT, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_RESOURCE, bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int) bankId))
                .body("creatorId", is(USER_ID_1))
                .body("balance", equalTo((float) DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ID);
        final double balance = JsonPath.from(guildAccountSaveResponse).getDouble(BALANCE);

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int) bankId))
                .body("balance", equalTo((float) balance))
                .body("id", equalTo((int) accountId));
    }


    /**
     * When close bank account for already closed bank
     * Then throw errors
     */
    @Test
    public void testCloseBankAccountOfACloseBank() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(DEPOSIT_AMOUNT, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_RESOURCE, bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int) bankId))
                .body("creatorId", is(USER_ID_1))
                .body("balance", equalTo((float) DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ID);
        final double balance = JsonPath.from(guildAccountSaveResponse).getDouble(BALANCE);

        //closing bank
        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_CLOSE_RESOURCE, bankId).then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int) bankId));

        //closing account
        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_CLOSE_RESOURCE, bankId, accountId).then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }


    /**
     * When adding members to bank
     * Then function should work successfully
     */
    @Test
    public void testAddMembersToBank() {
        //Creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //adding member account
        given().contentType(ContentType.JSON)
                .when().post(GUILD_BANK_MEMBER_RESOURCE,bankId, USER_ID_2).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("userId", is(USER_ID_2))
                .body("memberId", Matchers.notNullValue());
    }

    /**
     * When deposit amount to bank account
     * Then function should work successfully with correct account balance
     */
    @Test
    public void testDepositToBankAccount() {
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(DEPOSIT_AMOUNT,USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(USER_ID_1))
                .body("balance", equalTo((float)DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ID);

        //deposit 5000
        given().body(new AccountTransactionRequest(TRANSACTION_AMOUNT,USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_DEPOSIT_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int)bankId))
                .body("accountId", equalTo((int)accountId))
                .body("accountBalance", equalTo(15000f))
                .body("transactionAmount", equalTo((float)TRANSACTION_AMOUNT));
    }

    /**
     * When withdraw amount from bank account
     * Then function should work successfully with correct account balance
     */
    @Test
    public void testWithdrawFromBankAccount() {
        //creating bank
        final String guildBankSaveResponse = given().body(new CreateBankRequest(BANK_NAME, USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_RESOURCE).then().statusCode(HttpStatus.SC_CREATED).extract().asString();
        final long bankId = JsonPath.from(guildBankSaveResponse).getLong(ID);

        //Creating account
        final String guildAccountSaveResponse = given().body(new CreateAccountRequest(DEPOSIT_AMOUNT,USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_ACCOUNT_RESOURCE,bankId).then().statusCode(HttpStatus.SC_CREATED)
                .body("bankId", equalTo((int)bankId))
                .body("creatorId", is(USER_ID_1))
                .body("balance", equalTo((float)DEPOSIT_AMOUNT))
                .body("id", Matchers.notNullValue()).extract().asString();

        final long accountId = JsonPath.from(guildAccountSaveResponse).getLong(ID);

        //withdraw 5000
        given().body(new AccountTransactionRequest(TRANSACTION_AMOUNT,USER_ID_1)).contentType(ContentType.JSON)
                .when().post(GUILD_BANK_WITHDRAW_RESOURCE,bankId, accountId).then().statusCode(HttpStatus.SC_OK)
                .body("bankId", equalTo((int)bankId))
                .body("accountId", equalTo((int)accountId))
                .body("accountBalance", equalTo(5000f))
                .body("transactionAmount", equalTo((float)TRANSACTION_AMOUNT));
    }
}
