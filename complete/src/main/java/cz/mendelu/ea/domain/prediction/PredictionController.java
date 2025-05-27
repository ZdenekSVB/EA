package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
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

    @GetMapping(value = "", produces = "application/json")
    @Valid
    @Cacheable("predictions")
    public ArrayResponse<PredictionResponse> getAllPredictions() {
        return ArrayResponse.of(
                predictionService.getAllPredictions(),
                PredictionResponse::new
        );
    }

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

    @GetMapping(value = "/{id}", produces = "application/json")
    @Valid
    public ObjectResponse<PredictionResponse> getPrediction(@PathVariable Long id) {
        Prediction prediction = predictionService
                .getPredictionById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }

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

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "predictions", allEntries = true)
    public void deletePrediction(@PathVariable Long id) {
        predictionService.deletePrediction(id);
    }

    @PostMapping(value = "/predict", produces = "application/json")
    public ObjectResponse<PredictionResponse> predictHappiness(
            @RequestBody @Valid PredictionRequest request) {
        Prediction prediction = predictionService.predictFutureScore(request);
        return ObjectResponse.of(prediction, PredictionResponse::new);
    }
}