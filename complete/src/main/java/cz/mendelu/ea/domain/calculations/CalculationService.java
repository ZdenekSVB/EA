package cz.mendelu.ea.domain.calculations;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalculationService {

    private final Map<String, ICalculation> calculationMap;

    public CalculationService(List<ICalculation> calculations) {
        this.calculationMap = calculations.stream()
                .collect(java.util.stream.Collectors.toMap(
                        ICalculation::getType,
                        c -> c
                ));
    }

    public List<CalculationResponse> performAllCalculations() {
        List<CalculationResponse> results = new ArrayList<>();
        for (ICalculation calc : calculationMap.values()) {
            results.add(new CalculationResponse(calc.getType(), calc.getDescription(), calc.calculate()));
        }
        return results;
    }

    public CalculationResponse performCalculationByType(String type) {
        ICalculation calc = calculationMap.get(type);
        if (calc == null) throw new IllegalArgumentException("Unknown calculation type: " + type);
        return new CalculationResponse(type, calc.getDescription(), calc.calculate());
    }
}
