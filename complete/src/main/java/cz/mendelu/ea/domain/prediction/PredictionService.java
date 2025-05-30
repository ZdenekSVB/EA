// ===============================
// PredictionService.java (Service Layer)
// ===============================

package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.domain.happiness.HappinessService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service providing business logic for Prediction operations.
 */
@Service
public class PredictionService {

    private final PredictionRepository repository;
    private final CountryService countryService;
    private final HappinessService happinessService;

    public PredictionService(PredictionRepository repository,
                             CountryService countryService,
                             HappinessService happinessService) {
        this.repository = repository;
        this.countryService = countryService;
        this.happinessService = happinessService;
    }

    /**
     * Get all predictions in the system.
     */
    public List<Prediction> getAllPredictions() {
        List<Prediction> predictions = new ArrayList<>();
        repository.findAll().forEach(predictions::add);
        return predictions;
    }

    /**
     * Create a new prediction.
     * Score is automatically calculated.
     */
    public Prediction createPrediction(PredictionRequest request) {
        Country country = countryService.getCountryById(request.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found"));

        Prediction prediction = new Prediction();
        prediction.setYear(request.getYear());
        prediction.setCountry(country);
        prediction.setPredictedScore(calculatePredictedScore(country, request.getYear()));

        return repository.save(prediction);
    }

    /**
     * Get a prediction by its ID.
     */
    public Optional<Prediction> getPredictionById(Long id) {
        return repository.findById(id);
    }

    /**
     * Update a prediction by ID. Score is recalculated.
     */
    public Prediction updatePrediction(Long id, PredictionRequest request) {
        Prediction prediction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prediction not found"));

        Country country = countryService.getCountryById(request.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found"));

        prediction.setYear(request.getYear());
        prediction.setCountry(country);
        prediction.setPredictedScore(calculatePredictedScore(country, request.getYear()));

        return repository.save(prediction);
    }

    /**
     * Delete a prediction by ID.
     */
    public void deletePrediction(Long id) {
        repository.deleteById(id);
    }

    /**
     * Predict happiness score for a country/year (returns prediction but does not persist it).
     */
    public Prediction predictFutureScore(PredictionRequest request) {
        return createPrediction(request);
    }

    /**
     * Calculates a predicted happiness score.
     * (Temporary stub, replace with real model.)
     */
    private double calculatePredictedScore(Country country, int year) {
        // TODO: Replace with real algorithm
        return 5.5; // Default value
    }
}
