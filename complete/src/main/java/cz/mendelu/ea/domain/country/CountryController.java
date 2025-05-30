// ===============================
// CountryController.java (REST Controller)
// ===============================

package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;  // <--- pro Swagger/OpenAPI
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Country entity.
 */
@Tag(name = "Country API", description = "Endpoints for managing countries.")
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

    /**
     * Get all countries.
     * @return List of all countries.
     */
    @Operation(summary = "Get all countries", description = "Returns a list of all countries.")
    @GetMapping(produces = "application/json")
    @Cacheable("countries")
    public ArrayResponse<CountryResponse> getAllCountries() {
        return ArrayResponse.of(
                countryService.getAllCountries(),
                CountryResponse::new
        );
    }

    /**
     * Get detail for a specific country by ID.
     * @param id Country ID.
     * @return Country details.
     */
    @Operation(summary = "Get country by ID", description = "Returns a country by its ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ObjectResponse<CountryResponse> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(country, CountryResponse::new);
    }

    /**
     * Create a new country (requires JWT and rate-limits).
     * @param request Body with country name.
     * @param jwt User token.
     * @return Created country.
     */
    @Operation(summary = "Create new country", description = "Creates a new country with a unique name. Rate-limited and requires authentication.")
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

    /**
     * Update a country's name.
     * @param id Country ID.
     * @param request New data.
     * @return Updated country.
     */
    @Operation(summary = "Update country", description = "Updates the name of an existing country.")
    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "countries", allEntries = true)
    public ObjectResponse<CountryResponse> updateCountry(
            @PathVariable Long id,
            @RequestBody @Valid CountryRequest request) {

        Country country = countryService.updateCountry(id, request);
        return ObjectResponse.of(country, CountryResponse::new);
    }

    /**
     * Delete a country by ID.
     * @param id Country ID.
     */
    @Operation(summary = "Delete country", description = "Deletes a country by its ID (and cascades happiness + predictions).")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "countries", allEntries = true)
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }
}
