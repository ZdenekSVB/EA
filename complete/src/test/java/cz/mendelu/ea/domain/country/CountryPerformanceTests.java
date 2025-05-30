package cz.mendelu.ea.domain.country;

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
public class CountryPerformanceTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void getAllCountries_Performance() {
        given()
                .when()
                .get("/countries")
                .then()
                .statusCode(200)
                .time(lessThan(2000L)); // max 2s
    }

    @Test
    public void createCountry_Performance() {
        var newCountry = """
                {
                    "name": "PerfTest-" 
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(newCountry.replace("PerfTest-", "PerfTest-" + System.currentTimeMillis()))
                .when()
                .post("/countries")
                .then()
                .statusCode(201)
                .time(lessThan(1000L));
    }
}
