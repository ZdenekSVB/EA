package cz.mendelu.ea.domain.happiness;

import cz.mendelu.ea.domain.country.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Happiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Country country;

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

    public Happiness(Country country, int year) {
        this.country = country;
        this.year = year;
    }
}
