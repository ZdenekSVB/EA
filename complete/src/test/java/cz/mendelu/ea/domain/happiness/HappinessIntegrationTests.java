package cz.mendelu.ea.domain.happiness;

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
public class HappinessIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetAllHappiness() {
        given()
                .when()
                .get("/happiness")
                .then()
                .statusCode(200)
                .body("items", notNullValue());
    }

    @Test
    public void testCreateHappiness() {
        var happinessJson = """
                {
                  "countryId": 1,
                  "year": 2024,
                  "rank": 10,
                  "happinessScore": 7.2,
                  "gdp": 1.1,
                  "socialSupport": 2.2
                }
                """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(happinessJson)
                .when()
                .post("/happiness")
                .then()
                .statusCode(201)
                .body("content.year", is(2024))
                .body("content.countryId", is(1))
                .extract()
                .path("content.id");

        // Zkontrolujeme, že existuje
        given()
                .when()
                .get("/happiness/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.year", is(2024))
                .body("content.countryId", is(1));
    }

    @Test
    public void testCreateHappiness_BadRequest() {
        var badJson = """
                {
                  "countryId": null,
                  "year": 2024
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(badJson)
                .when()
                .post("/happiness")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetHappinessById_NotFound() {
        given()
                .when()
                .get("/happiness/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteHappiness() {
        // Nejprve vytvoříme záznam
        var happinessJson = """
                {
                  "countryId": 1,
                  "year": 2025
                }
                """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(happinessJson)
                .when()
                .post("/happiness")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        // Smažeme
        given()
                .when()
                .delete("/happiness/" + id)
                .then()
                .statusCode(204);

        // Už nesmí existovat
        given()
                .when()
                .get("/happiness/" + id)
                .then()
                .statusCode(404);
    }
    @Test
    public void testUpdateHappiness() {
        // Nejprve vytvoříme nový záznam
        var happinessJson = """
        {
          "countryId": 1,
          "year": 2023,
          "rank": 15,
          "happinessScore": 7.0,
          "gdp": 1.2,
          "socialSupport": 2.1
        }
        """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(happinessJson)
                .when()
                .post("/happiness")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        // Provedeme update
        var updatedJson = """
        {
          "countryId": 1,
          "year": 2023,
          "rank": 10,
          "happinessScore": 7.5,
          "gdp": 1.5,
          "socialSupport": 2.5
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/happiness/" + id)
                .then()
                .statusCode(202)
                .body("content.id", is(id))
                .body("content.rank", is(10))
                .body("content.happinessScore", is(7.5f)); // pozor na float/double

        // Kontrola změn
        given()
                .when()
                .get("/happiness/" + id)
                .then()
                .statusCode(200)
                .body("content.rank", is(10))
                .body("content.happinessScore", is(7.5f));
    }

}
