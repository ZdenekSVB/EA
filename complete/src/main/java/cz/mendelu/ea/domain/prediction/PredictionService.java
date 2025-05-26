package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.domain.happiness.HappinessService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Prediction> getAllPredictions() {
        List<Prediction> predictions = new ArrayList<>();
        repository.findAll().forEach(predictions::add);
        return predictions;
    }

    public Prediction createPrediction(PredictionRequest request) {
        Country country = countryService.getCountryById(request.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found"));

        Prediction prediction = new Prediction();
        prediction.setYear(request.getYear());
        prediction.setCountry(country);
        prediction.setPredictedScore(calculatePredictedScore(country, request.getYear()));

        return repository.save(prediction);
    }

    public Optional<Prediction> getPredictionById(Long id) {
        return repository.findById(id);
    }

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

    public void deletePrediction(Long id) {
        repository.deleteById(id);
    }

    public Prediction predictFutureScore(PredictionRequest request) {
        return createPrediction(request);
    }

    private double calculatePredictedScore(Country country, int year) {
        // Temporary implementation - replace with actual calculation
        return 5.5; // Default value
    }
}