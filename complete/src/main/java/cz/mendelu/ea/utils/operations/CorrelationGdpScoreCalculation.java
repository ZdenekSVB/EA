package cz.mendelu.ea.utils.operations;

import cz.mendelu.ea.domain.calculations.ICalculation;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CorrelationGdpScoreCalculation implements ICalculation {

    private final HappinessService happinessService;

    public CorrelationGdpScoreCalculation(HappinessService happinessService) {
        this.happinessService = happinessService;
    }

    @Override
    public String getType() {
        return "correlation-gdp-score";
    }

    @Override
    public String getDescription() {
        return "Pearson correlation between GDP per capita and Ladder score.";
    }

    @Override
    public Object calculate() {
        List<Happiness> data = happinessService.getAll().stream()
                .filter(h -> h.getGdp() != null && h.getHappinessScore() != null)
                .toList();

        int n = data.size();
        if (n < 2) return Map.of("error", "Not enough data");

        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        for (Happiness h : data) {
            double x = h.getGdp();
            double y = h.getHappinessScore();

            sumX += x;
            sumY += y;
            sumX2 += x * x;
            sumY2 += y * y;
            sumXY += x * y;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return Map.of("correlation", denominator == 0 ? null : numerator / denominator);
    }
}
