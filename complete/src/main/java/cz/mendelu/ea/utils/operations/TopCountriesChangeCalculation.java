package cz.mendelu.ea.utils.operations;

import cz.mendelu.ea.domain.calculations.ICalculation;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TopCountriesChangeCalculation implements ICalculation {

    private final HappinessService happinessService;

    public TopCountriesChangeCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    @Override
    public String getType() {
        return "top-countries-change";
    }

    @Override
    public String getDescription() {
        return "Country with the biggest positive change between two consecutive years.";
    }

    @Override
    public Object calculate() {
        Map<String, TreeMap<Integer, Double>> countryScores = new HashMap<>();
        for (Happiness h : happinessService.getAll()) {
            countryScores
                .computeIfAbsent(h.getCountry().getName(), k -> new TreeMap<>())
                .put(h.getYear(), h.getHappinessScore());
        }

        String bestCountry = null;
        double bestChange = Double.NEGATIVE_INFINITY;
        int fromYear = 0, toYear = 0;

        for (Map.Entry<String, TreeMap<Integer, Double>> entry : countryScores.entrySet()) {
            List<Map.Entry<Integer, Double>> years = new ArrayList<>(entry.getValue().entrySet());
            for (int i = 1; i < years.size(); i++) {
                double change = years.get(i).getValue() - years.get(i - 1).getValue();
                if (change > bestChange) {
                    bestChange = change;
                    bestCountry = entry.getKey();
                    fromYear = years.get(i - 1).getKey();
                    toYear = years.get(i).getKey();
                }
            }
        }

        return Map.of(
                "country", bestCountry,
                "change", bestChange,
                "from", fromYear,
                "to", toYear
        );
    }
}
