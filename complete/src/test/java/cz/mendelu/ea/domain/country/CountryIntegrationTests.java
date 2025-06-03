package cz.mendelu.ea.domain.country;

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
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class CountryIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetAllCountries() {
        given()
                .when()
                .get("/countries")
                .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id", containsInAnyOrder(1, 2))
                .body("items.name", containsInAnyOrder("Czech Republic", "Slovakia"));
    }

    @Test
    public void testCreateCountry() {
        var newCountry = """
                {
                    "name": "Austria"
                }
                """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(newCountry)
                .when()
                .post("/countries")
                .then()
                .statusCode(201)
                .body("content.name", is("Austria"))
                .extract()
                .path("content.id");

        // Kontrola, že country existuje
        given()
                .when()
                .get("/countries/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.name", is("Austria"));
    }

    @Test
    public void testCreateCountry_BadRequest() {
        var invalid = """
                {
                    "name": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when()
                .post("/countries")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateCountry_Conflict() {
        // Duplicita jména (už je v base-data.sql)
        var duplicate = """
                {
                    "name": "Czech Republic"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(duplicate)
                .when()
                .post("/countries")
                .then()
                .statusCode(anyOf(is(409), is(400))); // Některé API dává 409 (conflict), někdy 400 (bad request)
    }

    @Test
    public void testGetCountryById() {
        given()
                .when()
                .get("/countries/1")
                .then()
                .statusCode(200)
                .body("content.id", is(1))
                .body("content.name", is("Czech Republic"));
    }

    @Test
    public void testGetCountryById_NotFound() {
        given()
                .when()
                .get("/countries/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteCountry() {
        // Smažeme záznam
        given()
                .when()
                .delete("/countries/1")
                .then()
                .statusCode(204);

        // Kontrola, že už tam není
        given()
                .when()
                .get("/countries/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteCountry_NotFound() {
        given()
                .when()
                .delete("/countries/9999")
                .then()
                .statusCode(404);
    }
    @Test
    public void testUpdateCountry() {
        // Nejprve vytvořím novou zemi
        var newCountry = """
        {
            "name": "Poland"
        }
        """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(newCountry)
                .when()
                .post("/countries")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        // Změna názvu země přes PUT
        var update = """
        {
            "name": "Polska"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/countries/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.name", is("Polska"));

        // Kontrola, že změna proběhla
        given()
                .when()
                .get("/countries/" + id)
                .then()
                .statusCode(200)
                .body("content.name", is("Polska"));
    }

}
