package cz.mendelu.ea.domain.happiness;

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
public class HappinessPerformanceTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void getAllHappiness_Performance() {
        given()
                .when()
                .get("/happiness")
                .then()
                .statusCode(200)
                .time(lessThan(1000L));
    }

    @Test
    public void createHappiness_Performance() {
        var happinessJson = """
                {
                  "countryId": 1,
                  "year": %d,
                  "rank": 50,
                  "happinessScore": 6.1,
                  "gdp": 1.5,
                  "socialSupport": 2.3
                }
                """.formatted((int) (System.currentTimeMillis() % 10000));
        given()
                .contentType(ContentType.JSON)
                .body(happinessJson)
                .when()
                .post("/happiness")
                .then()
                .statusCode(201)
                .time(lessThan(5000L));
    }
}
