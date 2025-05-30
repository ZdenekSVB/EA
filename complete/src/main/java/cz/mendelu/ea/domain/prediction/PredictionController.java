// ===============================
// PredictionController.java (REST Controller)
// ===============================

package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation; // Swagger dokumentace
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing happiness predictions.
 */
@Tag(name = "Prediction API", description = "Endpoints for managing happiness predictions.")
@RestController
@RequestMapping("predictions")
@Validated
public class PredictionController {

    private final PredictionService predictionService;
    private final RateLimitingService rateLimitingService;

    @Autowired
    public PredictionController(PredictionService predictionService,
                                RateLimitingService rateLimitingService) {
        this.predictionService = predictionService;
        this.rateLimitingService = rateLimitingService;
    }

    /**
     * Get all predictions.
     */
    @Operation(summary = "Get all predictions", description = "Returns all happiness predictions for all countries and years.")
    @GetMapping(value = "", produces = "application/json")
    @Valid
    @Cacheable("predictions")
    public ArrayResponse<PredictionResponse> getAllPredictions() {
        return ArrayResponse.of(
                predictionService.getAllPredictions(),
                PredictionResponse::new
        );
    }

    /**
     * Create a new prediction. Requires authentication.
     */
    @Operation(summary = "Create prediction", description = "Creates a new happiness prediction for a country and year.")
    @PostMapping(value = "", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    @CacheEvict(value = "predictions", allEntries = true)
    public ObjectResponse<PredictionResponse> createPrediction(
            @RequestBody @Valid PredictionRequest predictionRequest,
            @AuthenticationPrincipal Jwt jwt) {

        Bucket bucket = rateLimitingService.resolveBucket(jwt);
        if (!bucket.tryConsume(1)) {
            throw new RateLimitException();
        }

        Prediction prediction = predictionService.createPrediction(predictionRequest);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }

    /**
     * Get a prediction by its ID.
     */
    @Operation(summary = "Get prediction by ID", description = "Returns details of a specific prediction.")
    @GetMapping(value = "/{id}", produces = "application/json")
    @Valid
    public ObjectResponse<PredictionResponse> getPrediction(@PathVariable Long id) {
        Prediction prediction = predictionService
                .getPredictionById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }

    /**
     * Update a prediction by ID.
     */
    @Operation(summary = "Update prediction", description = "Updates an existing prediction for a country and year.")
    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Valid
    @Transactional
    @CacheEvict(value = "predictions", allEntries = true)
    public ObjectResponse<PredictionResponse> updatePrediction(
            @PathVariable Long id,
            @RequestBody @Valid PredictionRequest predictionRequest) {

        Prediction prediction = predictionService.updatePrediction(id, predictionRequest);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }

    /**
     * Delete a prediction by ID.
     */
    @Operation(summary = "Delete prediction", description = "Deletes a prediction by its ID.")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "predictions", allEntries = true)
    public void deletePrediction(@PathVariable Long id) {
        predictionService.deletePrediction(id);
    }

    /**
     * Predict happiness (calculates a prediction but does not persist it).
     */
    @Operation(summary = "Predict happiness (calculate only)", description = "Calculates a prediction but does not persist it in the database.")
    @PostMapping(value = "/predict", produces = "application/json")
    public ObjectResponse<PredictionResponse> predictHappiness(
            @RequestBody @Valid PredictionRequest request) {
        Prediction prediction = predictionService.predictFutureScore(request);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }
}
