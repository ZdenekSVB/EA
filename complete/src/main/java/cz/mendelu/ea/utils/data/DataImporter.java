package cz.mendelu.ea.utils.data;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.country.CountryRepository;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessRepository;
import cz.mendelu.ea.domain.prediction.Prediction;
import cz.mendelu.ea.domain.prediction.PredictionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Component
public class DataImporter {

    private final CountryRepository countryRepository;
    private final HappinessRepository happinessRepository;
    private final PredictionRepository predictionRepository;

    public DataImporter(CountryRepository countryRepository,
                        HappinessRepository happinessRepository,
                        PredictionRepository predictionRepository) {
        this.countryRepository = countryRepository;
        this.happinessRepository = happinessRepository;
        this.predictionRepository = predictionRepository;
    }

    @Transactional
    public void importData() {
        if (countryRepository.count() > 0) {
            System.out.println("Data already exist. Skipping import.");
            return;
        }

        try (InputStream inputStream = new URL("https://happiness-report.s3.us-east-1.amazonaws.com/2025/Data+for+Figure+2.1+(2011%E2%80%932024).xlsx").openStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Map<String, Country> countryMap = new HashMap<>();
            Map<String, List<Double>> happinessHistory = new HashMap<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                int year = (int) row.getCell(0).getNumericCellValue();        // Year
                int rank = (int) row.getCell(1).getNumericCellValue();       // Rank
                String countryName = getCellString(row.getCell(2));          // Country name
                if (countryName == null) continue;

                Double happinessScore = getCellDouble(row.getCell(3));
                Double upperWhisker = getCellDouble(row.getCell(4));
                Double lowerWhisker = getCellDouble(row.getCell(5));
                Double gdp = getCellDouble(row.getCell(6));
                Double socialSupport = getCellDouble(row.getCell(7));
                Double healthyLifeExpectancy = getCellDouble(row.getCell(8));
                Double freedom = getCellDouble(row.getCell(9));
                Double generosity = getCellDouble(row.getCell(10));
                Double corruption = getCellDouble(row.getCell(11));
                Double dystopiaResidual = getCellDouble(row.getCell(12));

                // Najdi nebo vytvoř Country
                Country country = countryMap.computeIfAbsent(countryName, name -> {
                    return countryRepository.findByName(name).orElseGet(() -> {
                        Country c = new Country();
                        c.setName(name);
                        return countryRepository.save(c);
                    });
                });

                // Ulož záznam štěstí
                Happiness happiness = new Happiness();
                happiness.setCountry(country);
                happiness.setYear(year);
                happiness.setRank(rank);
                happiness.setHappinessScore(happinessScore);
                happiness.setUpperWhisker(upperWhisker);
                happiness.setLowerWhisker(lowerWhisker);
                happiness.setGdp(gdp);
                happiness.setSocialSupport(socialSupport);
                happiness.setHealthyLifeExpectancy(healthyLifeExpectancy);
                happiness.setFreedom(freedom);
                happiness.setGenerosity(generosity);
                happiness.setCorruption(corruption);
                happiness.setDystopiaResidual(dystopiaResidual);
                happinessRepository.save(happiness);

                // Shromažďuj historii pro predikci
                if (happinessScore != null) {
                    happinessHistory.computeIfAbsent(countryName, k -> new ArrayList<>()).add(happinessScore);
                }

                // Predikce (výpočet až po všem)
            }

            // Vytvoření predikcí (např. pro rok 2025)
            for (Map.Entry<String, List<Double>> entry : happinessHistory.entrySet()) {
                String countryName = entry.getKey();
                List<Double> scores = entry.getValue();
                if (scores.size() < 3) continue;

                double predicted = predictScore(scores);
                Country country = countryRepository.findByName(countryName).orElseThrow();
                Prediction prediction = new Prediction();
                prediction.setCountry(country);
                prediction.setYear(2025);
                prediction.setPredictedScore(predicted);
                predictionRepository.save(prediction);
            }

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download or import data", e);
        }
    }

    private String getCellString(Cell cell) {
        return (cell != null && cell.getCellType() == CellType.STRING)
                ? cell.getStringCellValue().trim()
                : (cell != null && cell.getCellType() == CellType.NUMERIC)
                ? String.valueOf((int) cell.getNumericCellValue())
                : null;
    }

    private Double getCellDouble(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                try {
                    yield Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> null;
        };
    }

    /**
     * Funkce pro predikci skóre – můžeš testovat samostatně.
     * Používá průměr posledních 3 hodnot.
     */
    public double predictScore(List<Double> scores) {
        int n = scores.size();
        return scores.subList(Math.max(0, n - 3), n)
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }
}
