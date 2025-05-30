// ===============================
// CountryResponse.java (DTO for Read/View)
// ===============================

package cz.mendelu.ea.domain.country;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO representing a country in API responses.
 * Includes basic country info and IDs of related happiness records.
 */
@Data
public class CountryResponse {

    private Long id;
    private String name;
    private List<Long> happinessRecordIds;

    /**
     * Create response DTO from a Country entity.
     */
    public CountryResponse(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.happinessRecordIds = country.getHappinessRecords().stream()
                .map(h -> h.getId())
                .collect(Collectors.toList());
    }
}
