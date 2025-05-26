package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.domain.account.AccountRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class CountryIntegrationTest {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetAllAccounts() {
        given()
        .when()
                .get("/accounts")
        .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id", containsInAnyOrder(1, 2))
                .body("items.ownerId", containsInAnyOrder(1, 2))
                .body("items.name", containsInAnyOrder("My account", "Savings for a car"))
                .body("items.balance", containsInAnyOrder(100.0f, 200.0f));
    }

    @Test
    public void testCreateAccount() {
        var newAccount = new AccountRequest(1L, "New account");
        int id = given()
                .contentType(ContentType.JSON)
                .body(newAccount)
        .when()
                .post("/accounts")
        .then()
                .statusCode(201)
         .extract()
                .path("content.id");

        when()
                .get("/accounts/" + id)
        .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.ownerId", is(1))
                .body("content.name", is("New account"))
                .body("content.balance", is(0.0f))
                .body("content.transactionCount", is(0));
    }

    @Test
    public void testCreateAccount_BadRequest() {
        var newStudent = new AccountRequest(null, "New account");

        given()
                .contentType(ContentType.JSON)
                .body(newStudent)
        .when()
                .post("/accounts")
        .then()
                .statusCode(400);
    }

    @Test
    public void testCreateAccount_OwnerNotFound() {
        var newStudent = new AccountRequest(999L, "New account");

        given()
                .contentType(ContentType.JSON)
                .body(newStudent)
        .when()
                .post("/accounts")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetAccountById() {
        given()
        .when()
                .get("/accounts/1")
        .then()
                .statusCode(200)
                .body("content.id", is(1))
                .body("content.ownerId", is(1))
                .body("content.name", is("My account"))
                .body("content.balance", is(100.0f));
    }

    @Test
    public void testGetAccountById_NotFound() {
        given()
                .when()
                .get("/accounts/999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateAccount() {
        var updatedAccount = new AccountRequest(2L, "Updated account");
        given()
                .contentType(ContentType.JSON)
                .body(updatedAccount)
        .when()
                .put("/accounts/1")
        .then()
                .statusCode(202);

        when()
                .get("/accounts/1")
        .then()
                .body("content.id", is(1))
                .body("content.ownerId", is(2))
                .body("content.name", is("Updated account"))
                .body("content.balance", is(100.0f));
    }


    @Test
    public void testUpdateAccount_SameOwner() {
        var updatedAccount = new AccountRequest(1L, "Updated account");
        given()
                .contentType(ContentType.JSON)
                .body(updatedAccount)
        .when()
                .put("/accounts/1")
        .then()
                .statusCode(202);

        when()
                .get("/accounts/1")
                .then()
                .body("content.ownerId", is(1));
    }

    @Test
    public void testUpdateAccount_NotFound() {
        var updatedAccount = new AccountRequest(1L, "Updated account");
        given()
                .contentType(ContentType.JSON)
                .body(updatedAccount)
        .when()
                .put("/accounts/999")
        .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateAccount_BadRequest() {
        var updatedAccount = new AccountRequest(null, "Updated account");
        given()
                .contentType(ContentType.JSON)
                .body(updatedAccount)
        .when()
                .put("/accounts/1")
        .then()
                .statusCode(400);
    }

    @Test
    public void testUpdateAccount_OwnerNotFound() {
        var updatedAccount = new AccountRequest(999L, "Updated account");
        given()
                .contentType(ContentType.JSON)
                .body(updatedAccount)
        .when()
                .put("/accounts/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteAccount() {
        given()
        .when()
                .delete("/accounts/1")
        .then()
                .statusCode(204);

        when().get("/accounts/1").then().statusCode(404);
    }


}