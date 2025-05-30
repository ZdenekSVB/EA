// ===============================
// Happiness.java (Entity)
// ===============================

package cz.mendelu.ea.domain.happiness;

import cz.mendelu.ea.domain.country.Country;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing annual happiness data for a specific country and year.
 * Holds various metrics reported in the World Happiness Report.
 */
@Entity
@Data
@NoArgsConstructor
public class Happiness {

    /** Primary key, auto-generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Country to which this record belongs.
     */
    @ManyToOne(optional = false)
    private Country country;

    /**
     * Year of the record (required).
     */
    @NotNull
    private int year;

    /** Rank of the country in this year (optional). */
    private Integer rank;

    /** Happiness score for this year (optional). */
    private Double happinessScore;

    /** Statistical upper whisker for the score (optional). */
    private Double upperWhisker;

    /** Statistical lower whisker for the score (optional). */
    private Double lowerWhisker;

    /** GDP per capita or similar metric (optional). */
    private Double gdp;

    /** Social support metric (optional). */
    private Double socialSupport;

    /** Healthy life expectancy metric (optional). */
    private Double healthyLifeExpectancy;

    /** Freedom metric (optional). */
    private Double freedom;

    /** Generosity metric (optional). */
    private Double generosity;

    /** Corruption perception metric (optional). */
    private Double corruption;

    /** Dystopia residual (optional). */
    private Double dystopiaResidual;

    /**
     * Constructor with required fields.
     */
    public Happiness(Country country, int year) {
        this.country = country;
        this.year = year;
    }
}
