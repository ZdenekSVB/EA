// ===============================
// PredictionResponse.java (DTO pro výstup)
// ===============================

package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.domain.country.CountryResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO representing a prediction in API responses.
 */
@Data
public class PredictionResponse {

    /** Unique identifier of the prediction. */
    @NotNull
    private Long id;

    /** Year for which the prediction is made. */
    @Min(2024)
    private int year;

    /** Predicted happiness score. */
    @NotNull
    private double predictedScore;

    /** Country for which the prediction applies. */
    @NotNull
    private CountryResponse country;

    /**
     * Constructs DTO from Prediction entity.
     */
    public PredictionResponse(Prediction prediction) {
        this.id = prediction.getId();
        this.year = prediction.getYear();
        this.predictedScore = prediction.getPredictedScore();
        this.country = new CountryResponse(prediction.getCountry());
    }
}
