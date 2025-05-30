// ===============================
// HappinessController.java (REST Controller)
// ===============================

package cz.mendelu.ea.domain.happiness;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation; // pro dokumentaci endpointů
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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
 * REST controller for managing Happiness records.
 */
@Tag(name = "Happiness API", description = "Endpoints for managing happiness data.")
@RestController
@RequestMapping("happiness")
@Validated
public class HappinessController {

    private final HappinessService happinessService;
    private final CountryService countryService;

    @Autowired
    public HappinessController(HappinessService happinessService, CountryService countryService) {
        this.happinessService = happinessService;
        this.countryService = countryService;
    }

    /**
     * Get all happiness records.
     */
    @Operation(summary = "Get all happiness records", description = "Returns all happiness records for all countries and years.")
    @GetMapping("")
    @Cacheable("happiness")
    public ArrayResponse<HappinessResponse> getAll() {
        return ArrayResponse.of(
                happinessService.getAll(),
                HappinessResponse::new
        );
    }

    /**
     * Create a new happiness record.
     */
    @Operation(summary = "Create happiness record", description = "Creates a new happiness record for a given country and year.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "happiness", allEntries = true)
    public ObjectResponse<HappinessResponse> create(@RequestBody @Valid HappinessRequest happinessRequest) {
        Happiness happiness = new Happiness();
        happinessRequest.toEntity(happiness, countryService);
        happinessService.create(happiness);
        return ObjectResponse.of(happiness, HappinessResponse::new);
    }

    /**
     * Get a specific happiness record by ID.
     */
    @Operation(summary = "Get happiness record by ID", description = "Returns happiness record details by its ID.")
    @GetMapping("/{id}")
    public ObjectResponse<HappinessResponse> get(@PathVariable Long id) {
        Happiness happiness = happinessService
                .getById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(happiness, HappinessResponse::new);
    }

    /**
     * Update a happiness record by ID.
     */
    @Operation(summary = "Update happiness record", description = "Updates an existing happiness record.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @CacheEvict(value = "happiness", allEntries = true)
    public ObjectResponse<HappinessResponse> update(@PathVariable Long id, @RequestBody @Valid HappinessRequest request) {
        Happiness happiness = happinessService.getById(id)
                .orElseThrow(NotFoundException::new);
        request.toEntity(happiness, countryService);
        happinessService.update(id, happiness);
        return ObjectResponse.of(happiness, HappinessResponse::new);
    }

    /**
     * Delete a happiness record by ID.
     */
    @Operation(summary = "Delete happiness record", description = "Deletes a happiness record by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "happiness", allEntries = true)
    public void delete(@PathVariable Long id) {
        happinessService.delete(id);
    }
}
