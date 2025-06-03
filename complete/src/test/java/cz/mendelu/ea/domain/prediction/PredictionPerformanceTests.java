package cz.mendelu.ea.domain.prediction;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

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
    public void getAllPredictions_Performance_Multi() {
        int iterations = 10;
        long maxAllowed = 1000L; // ms
        long sum = 0;
        long max = 0;
        long min = Long.MAX_VALUE;

        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();
            given()
                    .when()
                    .get("/predictions")
                    .then()
                    .statusCode(200);
            long duration = System.currentTimeMillis() - start;
            sum += duration;
            max = Math.max(max, duration);
            min = Math.min(min, duration);
            assertTrue(duration < maxAllowed, "GET /predictions request " + (i+1) + " took too long: " + duration + " ms");
        }
        long avg = sum / iterations;
        System.out.printf("Predictions GET performance: avg=%d ms, min=%d ms, max=%d ms\n", avg, min, max);
    }

    @Test
    public void createPrediction_Performance_Multi() {
        int iterations = 5;
        long maxAllowed = 2000L;
        long sum = 0;
        long max = 0;
        long min = Long.MAX_VALUE;

        for (int i = 0; i < iterations; i++) {
            var json = """
                    {
                      "countryId": 1,
                      "year": %d
                    }
                    """.formatted(3000 + i);

            long start = System.currentTimeMillis();
            given()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .when()
                    .post("/predictions")
                    .then()
                    .statusCode(201);
            long duration = System.currentTimeMillis() - start;
            sum += duration;
            max = Math.max(max, duration);
            min = Math.min(min, duration);
            assertTrue(duration < maxAllowed, "POST /predictions request " + (i+1) + " took too long: " + duration + " ms");
        }
        long avg = sum / iterations;
        System.out.printf("Predictions POST performance: avg=%d ms, min=%d ms, max=%d ms\n", avg, min, max);
    }
}
