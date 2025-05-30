// ===============================
// Prediction.java (Entity)
// ===============================

package cz.mendelu.ea.domain.prediction;

import cz.mendelu.ea.domain.country.Country;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a prediction of happiness score for a specific country and year.
 */
@Data
@NoArgsConstructor
@Entity
public class Prediction {

    /** Primary key, auto-generated. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Year for which the prediction applies. */
    private int year;

    /** Predicted happiness score for this country and year. */
    private double predictedScore;

    /**
     * Country for which the prediction is made.
     * Many predictions can refer to the same country.
     */
    @ManyToOne
    @JoinColumn(name = "country_id") // Matches migration
    private Country country;
}
