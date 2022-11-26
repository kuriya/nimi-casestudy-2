package com.nimi.guildbank.itest;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
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
    private static final String ID = "id";
    private static final String BANK_NAME = "TestBank";
    private static final String USER_ID_1 = "5620071b-5cd6-4468-97ab-744ab1f3e010";
    private static final double DEPOSIT_AMOUNT = 10000;

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
}
