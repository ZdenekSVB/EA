// ===============================
// PredictionRepository.java (Repozitář)
// ===============================

package cz.mendelu.ea.domain.prediction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing Prediction entities.
 */
@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    /** Find all predictions for a given country. */
    List<Prediction> findByCountryId(Long countryId);

    /** Find all predictions for a given year. */
    List<Prediction> findByYear(int year);

    /** Delete all predictions for a given country. */
    void deleteByCountryId(Long countryId);
}
