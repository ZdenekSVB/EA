package cz.mendelu.ea.domain.prediction;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PredictionPerformanceTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void getAllPredictions_Performance() {
        given()
                .when()
                .get("/predictions")
                .then()
                .statusCode(200)
                .time(lessThan(1000L));
    }

    @Test
    public void createPrediction_Performance() {
        var json = """
                {
                  "countryId": 1,
                  "year": %d
                }
                """.formatted(3000 + (int) (System.currentTimeMillis() % 1000));
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/predictions")
                .then()
                .statusCode(201)
                .time(lessThan(2000L));
    }
}
