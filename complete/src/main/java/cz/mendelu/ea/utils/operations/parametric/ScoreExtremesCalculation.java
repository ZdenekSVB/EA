package cz.mendelu.ea.utils.operations.parametric;

import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScoreExtremesCalculation {

    private final HappinessService happinessService;

    public ScoreExtremesCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    public Map<String, Object> calculate(int year) {
        List<Happiness> forYear = happinessService.getAll().stream()
                .filter(h -> h.getYear() == year)
                .toList();

        Optional<Happiness> max = forYear.stream().max(Comparator.comparingDouble(Happiness::getHappinessScore));
        Optional<Happiness> min = forYear.stream().min(Comparator.comparingDouble(Happiness::getHappinessScore));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", year);
        max.ifPresent(h -> result.put("max", Map.of("country", h.getCountry().getName(), "score", h.getHappinessScore())));
        min.ifPresent(h -> result.put("min", Map.of("country", h.getCountry().getName(), "score", h.getHappinessScore())));

        return result;
    }
}
