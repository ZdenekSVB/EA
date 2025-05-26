package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;

    public List<Country> getAllCountries() {
        return (List<Country>) repository.findAll();
    }

    public Optional<Country> getCountryById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Country createCountry(CountryRequest request) {
        Country country = new Country(request.getName());
        return repository.save(country);
    }

    @Transactional
    public Country updateCountry(Long id, CountryRequest request) {
        Country country = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));

        country.setName(request.getName());
        return repository.save(country);
    }

    @Transactional
    public void deleteCountry(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Country not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public List<Country> saveAll(List<Country> countries) {
        return (List<Country>) repository.saveAll(countries);
    }
}