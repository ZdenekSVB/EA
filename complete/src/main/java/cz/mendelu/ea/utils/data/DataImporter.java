package cz.mendelu.ea.utils.data;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.happiness.Happiness;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataImporter {

    private final ResourceLoader resourceLoader;

    public DataImporter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<Country> importCountries(Resource resource) throws IOException {
        List<Country> countries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(",");
                if (parts.length > 2) {
                    String countryName = parts[2].trim(); // Country name is in 3rd column
                    countries.add(new Country(countryName));
                }
            }
        }
        return countries;
    }

    public List<Happiness> importHappinessData(Resource resource, List<Country> countries) throws IOException {
        List<Happiness> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    Happiness record = new Happiness();
                    // Parse and set all happiness fields
                    // Example: record.setYear(Integer.parseInt(parts[0].trim()));
                    // Add to records list
                }
            }
        }
        return records;
    }
}