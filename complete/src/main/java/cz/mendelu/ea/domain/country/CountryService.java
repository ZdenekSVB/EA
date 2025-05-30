// ===============================
// CountryService.java (Service Layer)
// ===============================

package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.domain.prediction.PredictionRepository;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service providing business logic for Country operations.
 */
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;
    private final PredictionRepository predictionRepository;

    /**
     * Get all countries from DB.
     */
    public List<Country> getAllCountries() {
        return (List<Country>) repository.findAll();
    }

    /**
     * Get a single country by ID.
     */
    public Optional<Country> getCountryById(Long id) {
        return repository.findById(id);
    }

    /**
     * Create a new country.
     */
    @Transactional
    public Country createCountry(CountryRequest request) {
        Country country = new Country(request.getName());
        return repository.save(country);
    }

    /**
     * Update an existing country.
     */
    @Transactional
    public Country updateCountry(Long id, CountryRequest request) {
        Country country = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));
        country.setName(request.getName());
        return repository.save(country);
    }

    /**
     * Delete a country and all associated predictions (happiness handled by orphanRemoval).
     */
    @Transactional
    public void deleteCountry(Long id) {
        Country country = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));
        // 1) Delete all predictions for this country
        predictionRepository.deleteByCountryId(id);
        // 2) Delete country (happiness orphanRemoval is automatic)
        repository.delete(country);
    }

    /**
     * Bulk-save countries.
     */
    @Transactional
    public List<Country> saveAll(List<Country> countries) {
        return (List<Country>) repository.saveAll(countries);
    }
}
