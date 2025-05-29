package cz.mendelu.ea.utils.operations;

import cz.mendelu.ea.domain.calculations.ICalculation;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class AverageScorePerYearCalculation implements ICalculation {

    private final HappinessService happinessService;

    public AverageScorePerYearCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    @Override
    public String getType() {
        return "average-score";
    }

    @Override
    public String getDescription() {
        return "Average global happiness score (Ladder score) per year.";
    }

    @Override
    public Object calculate() {
        return happinessService.getAll().stream()
                .collect(Collectors.groupingBy(
                        Happiness::getYear,
                        TreeMap::new,
                        Collectors.averagingDouble(Happiness::getHappinessScore)
                ));
    }
}
