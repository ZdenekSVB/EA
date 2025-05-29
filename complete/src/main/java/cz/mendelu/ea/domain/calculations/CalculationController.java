package cz.mendelu.ea.domain.calculations;

import cz.mendelu.ea.utils.operations.parametric.ScoreDecompositionCalculation;
import cz.mendelu.ea.utils.operations.parametric.ScoreExtremesCalculation;
import cz.mendelu.ea.utils.operations.parametric.ScoreTrendCalculation;
import cz.mendelu.ea.utils.response.ArrayResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/calculations")
public class CalculationController {

    private final CalculationService calculationService;
    private final ScoreTrendCalculation scoreTrendCalculation;
    private final ScoreExtremesCalculation scoreExtremesCalculation;
    private final ScoreDecompositionCalculation scoreDecompositionCalculation;

    public CalculationController(
            CalculationService calculationService,
            ScoreTrendCalculation scoreTrendCalculation,
            ScoreExtremesCalculation scoreExtremesCalculation,
            ScoreDecompositionCalculation scoreDecompositionCalculation
    ) {
        this.calculationService = calculationService;
        this.scoreTrendCalculation = scoreTrendCalculation;
        this.scoreExtremesCalculation = scoreExtremesCalculation;
        this.scoreDecompositionCalculation = scoreDecompositionCalculation;
    }

    @GetMapping("")
    public ArrayResponse<CalculationResponse> getAllCalculations() {
        List<CalculationResponse> results = calculationService.performAllCalculations();
        return ArrayResponse.of(results, Function.identity());
    }

    @GetMapping("/{type}")
    public CalculationResponse getCalculation(@PathVariable String type) {
        return calculationService.performCalculationByType(type);
    }

    @GetMapping("/score-trend/{country}")
    public Map<String, Double> getScoreTrend(@PathVariable String country) {
        return scoreTrendCalculation.calculate(country);
    }

    @GetMapping("/extremes/{year}")
    public Map<String, Object> getScoreExtremes(@PathVariable int year) {
        return scoreExtremesCalculation.calculate(year);
    }

    @GetMapping("/score-decomposition/{country}/{year}")
    public Map<String, Double> getDecomposition(
            @PathVariable String country,
            @PathVariable int year
    ) {
        return scoreDecompositionCalculation.calculate(country, year);
    }
}
