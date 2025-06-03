package cz.mendelu.ea.domain.prediction;

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
public class PredictionIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetAllPredictions() {
        given()
                .when()
                .get("/predictions")
                .then()
                .statusCode(200)
                .body("items", notNullValue());
    }

    @Test
    public void testCreatePrediction() {
        var json = """
                {
                  "countryId": 1,
                  "year": 2025
                }
                """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/predictions")
                .then()
                .statusCode(201)
                .body("content.year", is(2025))
                .body("content.country.id", is(1))
                .extract()
                .path("content.id");

        // Kontrola že existuje
        given()
                .when()
                .get("/predictions/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.year", is(2025))
                .body("content.country.id", is(1));
    }

    @Test
    public void testCreatePrediction_BadRequest() {
        var badJson = """
                {
                  "countryId": null,
                  "year": 2025
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(badJson)
                .when()
                .post("/predictions")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetPredictionById_NotFound() {
        given()
                .when()
                .get("/predictions/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeletePrediction() {
        // Nejprve vytvoříme predikci
        var json = """
                {
                  "countryId": 1,
                  "year": 2028
                }
                """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/predictions")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        // Smažeme
        given()
                .when()
                .delete("/predictions/" + id)
                .then()
                .statusCode(204);

        // Už nesmí existovat
        given()
                .when()
                .get("/predictions/" + id)
                .then()
                .statusCode(404);
    }
    @Test
    public void testUpdatePrediction() {
        // Nejprve vytvoříme predikci
        var json = """
        {
          "countryId": 1,
          "year": 2030
        }
        """;

        int id = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/predictions")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        // Provedeme update
        var updated = """
        {
          "countryId": 1,
          "year": 2035
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/predictions/" + id)
                .then()
                .statusCode(202)
                .body("content.id", is(id))
                .body("content.year", is(2035));

        // Kontrola změny
        given()
                .when()
                .get("/predictions/" + id)
                .then()
                .statusCode(200)
                .body("content.year", is(2035));
    }
    @Test
    public void testPredictionCalculateOnly() {
        var json = """
        {
          "countryId": 1,
          "year": 2040
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/predictions/predict")
                .then()
                .statusCode(200)
                .body("content.year", is(2040));
    }

}
