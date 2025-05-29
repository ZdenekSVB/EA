package cz.mendelu.ea.utils.operations;

import cz.mendelu.ea.domain.calculations.ICalculation;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BestImprovement5YCalculation implements ICalculation {

    private final HappinessService happinessService;

    public BestImprovement5YCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    @Override
    public String getType() {
        return "best-improvement-5y";
    }

    @Override
    public String getDescription() {
        return "Best improvement in happiness score over the past 5 years.";
    }

    @Override
    public Object calculate() {
        int currentYear = happinessService.getAll().stream().mapToInt(Happiness::getYear).max().orElse(2024);
        int prevYear = currentYear - 5;

        Map<String, Double> start = new HashMap<>();
        Map<String, Double> end = new HashMap<>();

        for (Happiness h : happinessService.getAll()) {
            String country = h.getCountry().getName();
            if (h.getYear() == prevYear) start.put(country, h.getHappinessScore());
            if (h.getYear() == currentYear) end.put(country, h.getHappinessScore());
        }

        String bestCountry = null;
        double maxDiff = Double.NEGATIVE_INFINITY;

        for (String country : end.keySet()) {
            if (start.containsKey(country)) {
                double diff = end.get(country) - start.get(country);
                if (diff > maxDiff) {
                    maxDiff = diff;
                    bestCountry = country;
                }
            }
        }

        return Map.of(
                "country", bestCountry,
                "score_diff", maxDiff,
                "from", prevYear,
                "to", currentYear
        );
    }
}
