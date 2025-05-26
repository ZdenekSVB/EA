package cz.mendelu.ea.utils.data;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.domain.happiness.Happiness;
import cz.mendelu.ea.domain.happiness.HappinessService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
public class Seeder {

    private final CountryService countryService;
    private final HappinessService happinessService;
    private final ResourceLoader resourceLoader;
    private final DataImporter dataImporter;

    public Seeder(CountryService countryService,
                  HappinessService happinessService,
                  ResourceLoader resourceLoader,
                  DataImporter dataImporter) {
        this.countryService = countryService;
        this.happinessService = happinessService;
        this.resourceLoader = resourceLoader;
        this.dataImporter = dataImporter;
    }

    @Transactional
    public void seed() {
        if (countryService.getAllCountries().isEmpty()) {
            try {
                Resource resource = resourceLoader.getResource("classpath:data/world-happiness-data.csv");
                List<Country> countries = dataImporter.importCountries(resource);
                countryService.saveAll(countries);

                List<Happiness> happinessData = dataImporter.importHappinessData(resource, countries);
                happinessService.saveAll(happinessData);
            } catch (IOException e) {
                throw new RuntimeException("Failed to seed data", e);
            }
        }
    }
}