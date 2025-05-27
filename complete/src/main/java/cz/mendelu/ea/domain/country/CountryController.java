package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("countries")
@Validated
public class CountryController {

    private final CountryService countryService;
    private final RateLimitingService rateLimitingService;

    @Autowired
    public CountryController(CountryService countryService,
                             RateLimitingService rateLimitingService) {
        this.countryService = countryService;
        this.rateLimitingService = rateLimitingService;
    }

    @GetMapping(produces = "application/json")
    @Cacheable("countries")
    public ArrayResponse<CountryResponse> getAllCountries() {
        return ArrayResponse.of(
                countryService.getAllCountries(),
                CountryResponse::new
        );
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ObjectResponse<CountryResponse> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(country, CountryResponse::new);
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "countries", allEntries = true)
    public ObjectResponse<CountryResponse> createCountry(
            @RequestBody @Valid CountryRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        Bucket bucket = rateLimitingService.resolveBucket(jwt);
        if (!bucket.tryConsume(1)) {
            throw new RateLimitException();
        }

        Country country = countryService.createCountry(request);
        return ObjectResponse.of(country, CountryResponse::new);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "countries", allEntries = true)
    public ObjectResponse<CountryResponse> updateCountry(
            @PathVariable Long id,
            @RequestBody @Valid CountryRequest request) {

        Country country = countryService.updateCountry(id, request);
        return ObjectResponse.of(country, CountryResponse::new);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "countries", allEntries = true)
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }
}