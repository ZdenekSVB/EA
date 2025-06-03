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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void getAllCountries_Performance_Multi() {
        int iterations = 10;
        long maxAllowed = 2000L; // ms
        long sum = 0;
        long max = 0;
        long min = Long.MAX_VALUE;

        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();
            given()
                    .when()
                    .get("/countries")
                    .then()
                    .statusCode(200);
            long duration = System.currentTimeMillis() + 1 - start;
            sum += duration;
            max = Math.max(max, duration);
            min = Math.min(min, duration);
            assertTrue(duration < maxAllowed, "Request " + (i+1) + " took too long: " + duration + " ms");
        }
        long avg = sum / iterations;
        System.out.printf("Countries GET performance: avg=%d ms, min=%d ms, max=%d ms\n", avg, min, max);
    }

}
