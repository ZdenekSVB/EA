package cz.mendelu.ea.utils.operations.parametric;

import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScoreTrendCalculation {

    private final HappinessService happinessService;

    public ScoreTrendCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    public Map<String, Double> calculate(String countryName) {
        return happinessService.getAll().stream()
                .filter(h -> h.getCountry().getName().equalsIgnoreCase(countryName))
                .sorted(Comparator.comparingInt(Happiness::getYear))
                .collect(Collectors.toMap(
                        h -> String.valueOf(h.getYear()),
                        Happiness::getHappinessScore,
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }
}
