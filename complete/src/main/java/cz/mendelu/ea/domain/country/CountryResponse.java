package cz.mendelu.ea.domain.country;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CountryResponse {

    private Long id;
    private String name;
    private List<Long> happinessRecordIds;

    public CountryResponse(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.happinessRecordIds = country.getHappinessRecords().stream()
                .map(h -> h.getId())
                .collect(Collectors.toList());
    }
}