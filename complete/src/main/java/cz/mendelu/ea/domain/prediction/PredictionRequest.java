// ===============================
// PredictionRequest.java (DTO pro vstup)
// ===============================

package cz.mendelu.ea.domain.prediction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating or updating a Prediction.
 */
@Data
public class PredictionRequest {

    /** Target country ID (required). */
    @NotNull
    private Long countryId;

    /** Target year (must be >= 2024). */
    @Min(2024)
    private int year;

    // Score is computed, not passed from client
}
