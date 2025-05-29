package cz.mendelu.ea.domain.calculations;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculationResponse {
    private String type;
    private String description;
    private Object result;
}
