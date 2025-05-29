package cz.mendelu.ea.utils.operations;

import cz.mendelu.ea.domain.calculations.ICalculation;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StableCountriesCalculation implements ICalculation {

    private final HappinessService happinessService;

    public StableCountriesCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    @Override
    public String getType() {
        return "stable-countries";
    }

    @Override
    public String getDescription() {
        return "Countries with lowest variation (standard deviation) in happiness score.";
    }

    @Override
    public Object calculate() {
        Map<String, List<Double>> scores = happinessService.getAll().stream()
                .collect(Collectors.groupingBy(
                        h -> h.getCountry().getName(),
                        Collectors.mapping(Happiness::getHappinessScore, Collectors.toList())
                ));

        return scores.entrySet().stream()
                .filter(e -> e.getValue().size() >= 2)
                .map(e -> Map.entry(e.getKey(), stddev(e.getValue())))
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double stddev(List<Double> list) {
        double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return Math.sqrt(list.stream().mapToDouble(x -> (x - avg) * (x - avg)).average().orElse(0));
    }
}
