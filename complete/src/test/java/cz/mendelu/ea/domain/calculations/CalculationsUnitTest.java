package cz.mendelu.ea.domain.calculations;

import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import cz.mendelu.ea.utils.operations.*;
import cz.mendelu.ea.utils.operations.parametric.ScoreDecompositionCalculation;
import cz.mendelu.ea.utils.operations.parametric.ScoreExtremesCalculation;
import cz.mendelu.ea.utils.operations.parametric.ScoreTrendCalculation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for all 8 analytics calculations in the calculations module.
 * Each test checks the expected output for a specific calculation class using mocked data.
 */
public class CalculationsUnitTest {

    /** Test for correlation between GDP and happiness score. */
    @Test
    void testCorrelationGdpScoreCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setGdp(1.0); h1.setHappinessScore(5.0);
        Happiness h2 = new Happiness(); h2.setGdp(2.0); h2.setHappinessScore(6.0);
        when(service.getAll()).thenReturn(List.of(h1, h2));

        CorrelationGdpScoreCalculation calc = new CorrelationGdpScoreCalculation(service);
        Map<String, Object> result = (Map<String, Object>) calc.calculate();

        assertTrue(result.containsKey("correlation"));
    }

    /** Test for average happiness score per year calculation. */
    @Test
    void testAverageScorePerYearCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setYear(2020); h1.setHappinessScore(6.0);
        Happiness h2 = new Happiness(); h2.setYear(2020); h2.setHappinessScore(4.0);
        when(service.getAll()).thenReturn(List.of(h1, h2));

        AverageScorePerYearCalculation calc = new AverageScorePerYearCalculation(service);
        Map<Integer, Double> result = (Map<Integer, Double>) calc.calculate();

        assertEquals(1, result.size());
        assertEquals(5.0, result.get(2020));
    }

    /** Test for detection of stable countries in happiness scores. */
    @Test
    void testStableCountriesCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setCountry(new cz.mendelu.ea.domain.country.Country("Stableland"));
        h1.setHappinessScore(5.0); h1.setYear(2020);

        Happiness h2 = new Happiness(); h2.setCountry(new cz.mendelu.ea.domain.country.Country("Stableland"));
        h2.setHappinessScore(5.1); h2.setYear(2021);

        when(service.getAll()).thenReturn(List.of(h1, h2));

        StableCountriesCalculation calc = new StableCountriesCalculation(service);
        List<String> result = (List<String>) calc.calculate();

        assertTrue(result.contains("Stableland"));
    }

    /** Test for calculation of the country with top happiness score change between years. */
    @Test
    void testTopCountriesChangeCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setCountry(new cz.mendelu.ea.domain.country.Country("Boomland")); h1.setYear(2020); h1.setHappinessScore(3.0);
        Happiness h2 = new Happiness(); h2.setCountry(new cz.mendelu.ea.domain.country.Country("Boomland")); h2.setYear(2021); h2.setHappinessScore(7.0);

        when(service.getAll()).thenReturn(List.of(h1, h2));

        TopCountriesChangeCalculation calc = new TopCountriesChangeCalculation(service);
        Map<String, Object> result = (Map<String, Object>) calc.calculate();

        assertEquals("Boomland", result.get("country"));
        assertEquals(4.0, result.get("change"));
    }

    /** Test for best improvement in happiness score over 5 years. */
    @Test
    void testBestImprovement5YCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setCountry(new cz.mendelu.ea.domain.country.Country("Futureland")); h1.setYear(2018); h1.setHappinessScore(2.0);
        Happiness h2 = new Happiness(); h2.setCountry(new cz.mendelu.ea.domain.country.Country("Futureland")); h2.setYear(2023); h2.setHappinessScore(6.5);

        when(service.getAll()).thenReturn(List.of(h1, h2));

        BestImprovement5YCalculation calc = new BestImprovement5YCalculation(service);
        Map<String, Object> result = (Map<String, Object>) calc.calculate();

        assertEquals("Futureland", result.get("country"));
        assertEquals(4.5, result.get("score_diff"));
    }

    /** Test for happiness score trend calculation for a given country. */
    @Test
    void testScoreTrendCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h1 = new Happiness(); h1.setYear(2020); h1.setHappinessScore(5.0);
        h1.setCountry(new cz.mendelu.ea.domain.country.Country("Czechia"));

        Happiness h2 = new Happiness(); h2.setYear(2021); h2.setHappinessScore(6.0);
        h2.setCountry(new cz.mendelu.ea.domain.country.Country("Czechia"));

        when(service.getAll()).thenReturn(List.of(h1, h2));

        ScoreTrendCalculation calc = new ScoreTrendCalculation(service);
        Map<String, Double> result = calc.calculate("Czechia");

        assertEquals(2, result.size());
        assertEquals(5.0, result.get("2020"));
        assertEquals(6.0, result.get("2021"));
    }

    /** Test for decomposition of the happiness score into components. */
    @Test
    void testScoreDecompositionCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness h = new Happiness();
        h.setCountry(new cz.mendelu.ea.domain.country.Country("Czechia"));
        h.setYear(2021);
        h.setGdp(1.0); h.setSocialSupport(1.0); h.setHealthyLifeExpectancy(1.0);
        h.setFreedom(1.0); h.setGenerosity(1.0); h.setCorruption(1.0);
        h.setHappinessScore(6.5);

        when(service.getAll()).thenReturn(List.of(h));

        ScoreDecompositionCalculation calc = new ScoreDecompositionCalculation(service);
        Map<String, Double> result = calc.calculate("Czechia", 2021);

        assertEquals(7, result.size());
        assertEquals(0.5, result.get("Residual"));
    }

    /** Test for min/max extremes in happiness score for a given year. */
    @Test
    void testScoreExtremesCalculation() {
        HappinessService service = mock(HappinessService.class);

        Happiness min = new Happiness();
        min.setCountry(new cz.mendelu.ea.domain.country.Country("Minland"));
        min.setYear(2020); min.setHappinessScore(2.0);

        Happiness max = new Happiness();
        max.setCountry(new cz.mendelu.ea.domain.country.Country("Maxland"));
        max.setYear(2020); max.setHappinessScore(8.0);

        when(service.getAll()).thenReturn(List.of(min, max));

        ScoreExtremesCalculation calc = new ScoreExtremesCalculation(service);
        Map<String, Object> result = calc.calculate(2020);

        assertEquals("Minland", ((Map<?, ?>) result.get("min")).get("country"));
        assertEquals("Maxland", ((Map<?, ?>) result.get("max")).get("country"));
    }
}
