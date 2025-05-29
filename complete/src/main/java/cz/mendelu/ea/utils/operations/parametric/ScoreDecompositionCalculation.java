package cz.mendelu.ea.utils.operations.parametric;

import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScoreDecompositionCalculation {

    private final HappinessService happinessService;

    public ScoreDecompositionCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    public Map<String, Double> calculate(String countryName, int year) {
        Optional<Happiness> entry = happinessService.getAll().stream()
                .filter(h -> h.getCountry().getName().equalsIgnoreCase(countryName) && h.getYear() == year)
                .findFirst();

        if (entry.isEmpty()) return Map.of();

        Happiness h = entry.get();
        Map<String, Double> factors = new LinkedHashMap<>();
        factors.put("GDP", h.getGdp());
        factors.put("SocialSupport", h.getSocialSupport());
        factors.put("HealthyLifeExpectancy", h.getHealthyLifeExpectancy());
        factors.put("Freedom", h.getFreedom());
        factors.put("Generosity", h.getGenerosity());
        factors.put("Corruption", h.getCorruption());
        factors.put("Residual", h.getHappinessScore()
                - h.getGdp()
                - h.getSocialSupport()
                - h.getHealthyLifeExpectancy()
                - h.getFreedom()
                - h.getGenerosity()
                - h.getCorruption());
        return factors;
    }
}
