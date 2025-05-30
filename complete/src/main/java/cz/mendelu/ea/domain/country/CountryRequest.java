// ===============================
// CountryRequest.java (DTO for Create/Update)
// ===============================

package cz.mendelu.ea.domain.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating/updating a country.
 */
@Data
public class CountryRequest {

    /** Name of the country (must be unique, not blank, max 100 chars). */
    @NotBlank
    @Size(max = 100)
    private String name;
}
