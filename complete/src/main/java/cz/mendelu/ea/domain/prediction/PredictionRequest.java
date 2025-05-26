package cz.mendelu.ea.domain.prediction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PredictionRequest {

    @NotNull
    private Long countryId;

    @Min(2024)
    private int year;

    // No need for toEntity method - conversion happens in service
}