// ===============================
// HappinessRequest.java (DTO for create/update)
// ===============================

package cz.mendelu.ea.domain.happiness;

import cz.mendelu.ea.domain.country.Country;
import cz.mendelu.ea.domain.country.CountryService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating or updating Happiness records.
 * Populates a Happiness entity using toEntity().
 */
@Data
public class HappinessRequest {

    /** Country ID (required). */
    @NotNull
    private Long countryId;

    /** Year (required). */
    @NotNull
    private int year;

    private Integer rank;
    private Double happinessScore;
    private Double upperWhisker;
    private Double lowerWhisker;
    private Double gdp;
    private Double socialSupport;
    private Double healthyLifeExpectancy;
    private Double freedom;
    private Double generosity;
    private Double corruption;
    private Double dystopiaResidual;

    /**
     * Fill given entity from this DTO.
     * @param happiness Entity to fill.
     * @param countryService Service for fetching Country.
     */
    public void toEntity(Happiness happiness, CountryService countryService) {
        Country country = countryService.getCountryById(countryId)
                .orElseThrow(() -> new NotFoundException("Country not found"));
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
    }
}
