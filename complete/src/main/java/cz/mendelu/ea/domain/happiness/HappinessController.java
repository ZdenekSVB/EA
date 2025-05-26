package cz.mendelu.ea.domain.happiness;

import cz.mendelu.ea.config.RateLimitingService;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.exceptions.RateLimitException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.github.bucket4j.Bucket;
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

    @GetMapping("")
    @Cacheable("happiness")
    public ArrayResponse<HappinessResponse> getAll() {
        return ArrayResponse.of(
                happinessService.getAll(),
                HappinessResponse::new
        );
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "happiness", allEntries = true)
    public ObjectResponse<HappinessResponse> create(@RequestBody @Valid HappinessRequest happinessRequest) {
        Happiness happiness = new Happiness();
        happinessRequest.toEntity(happiness, countryService);
        happinessService.create(happiness);
        return ObjectResponse.of(happiness, HappinessResponse::new);
    }

    @GetMapping("/{id}")
    public ObjectResponse<HappinessResponse> get(@PathVariable Long id) {
        Happiness happiness = happinessService
                .getById(id)
                .orElseThrow(NotFoundException::new);
        return ObjectResponse.of(happiness, HappinessResponse::new);
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "happiness", allEntries = true)
    public void delete(@PathVariable Long id) {
        happinessService.delete(id);
    }
}
