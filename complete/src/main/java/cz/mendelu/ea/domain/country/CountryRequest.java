package cz.mendelu.ea.domain.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryRequest {

    @NotBlank
    @Size(max = 100)
    private String name;
}