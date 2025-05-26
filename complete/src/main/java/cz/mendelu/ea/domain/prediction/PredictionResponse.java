package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.domain.country.CountryResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PredictionResponse {

    @NotNull
    private Long id;

    @Min(2024)
    private int year;

    @NotNull
    private double predictedScore;

    @NotNull
    private CountryResponse country;

    public PredictionResponse(Prediction prediction) {
        this.id = prediction.getId();
        this.year = prediction.getYear();
        this.predictedScore = prediction.getPredictedScore();
        this.country = new CountryResponse(prediction.getCountry());
    }
}