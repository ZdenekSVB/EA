package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.domain.prediction.PredictionRepository;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;
    private final PredictionRepository predictionRepository; // přidej to do konstruktoru

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
        Country country = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found with id: " + id));

        // 1) Smazat všechny predikce navázané na tuto zemi
        predictionRepository.deleteByCountryId(id);

        // 2) Smazat samotnou country (a orphanRemoval se postará o happiness)
        repository.delete(country);
    }




    @Transactional
    public List<Country> saveAll(List<Country> countries) {
        return (List<Country>) repository.saveAll(countries);
    }
}